/*
 * Created on 2005/01/24
 * Copyright (C) 2005 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.mikity.ios.control;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.TimerTask;

import org.mklab.mikity.control.AnimationTaskListener;
import org.mklab.mikity.model.GroupObjectManager;
import org.mklab.mikity.renderer.ObjectRenderer;

import org.robovm.apple.foundation.NSProcessInfo;

/**
 * アニメーションを実行するクラスです。
 * 
 * @author miki
 * @version $Revision: 1.6 $.2005/01/24
 */
public class AnimationTask extends TimerTask {
  /** アニメーションの速度倍率(1.0のときに実時間で再生) */
  private double speedScale = 1.0;
  /** グループマネージャ　 */
  private GroupObjectManager manager;
  /** リスナー　 */
  private List<AnimationTaskListener> listeners = new ArrayList<AnimationTaskListener>();
  /** 現在の時間　 */
  private double currentTime = 0.0;
  /** 開始時間　 */
  private final double startTime;
  /** 終了時間　 */
  private double endTime = 0.0;
  /** レンダラー */
  private ObjectRenderer renderer;
  /** 遅延時間。 */
  private long delayTime;

  /**
   * 新しく生成された<code>AnimationTask</code>オブジェクトを初期化します。
   * 
   * @param startTime 開始時間
   * @param endTime 終了時間
   * @param manager グループマネージャー
   * @param renderer レンダラー
   * @param delayTime 遅延時間
   */
  public AnimationTask(double startTime, double endTime, GroupObjectManager manager, ObjectRenderer renderer, long delayTime) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.currentTime = startTime;
    this.manager = manager;
    this.renderer = renderer;
    this.delayTime = delayTime;
  }


  /**
   * {@link AnimationTaskListener}を登録します。
   * 
   * @param listener リスナ
   */
  public void addAnimationTaskListener(AnimationTaskListener listener) {
    this.listeners.add(listener);
  }

  /**
   * アニメーションの速度倍率を返します。 
   * 
   * 速度は1.0のときに実時間で再生します。
   * 
   * @return アニメーションの速度倍率
   */
  public double getSpeedScale() {
    return this.speedScale;
  }

  /**
   * アニメーションの速度倍率を設定します。
   * 
   * 速度が1.0のときに実時間で再生します。
   * 
   * @param speedScale アニメーションの速度倍率
   */
  public void setSpeedScale(double speedScale) {
    this.speedScale = speedScale;
  }

  /**
   * 再生ボタンが押されると実行されます。
   * 
   * {@inheritDoc}
   */
  @Override
  public void run() {
    if (this.currentTime == this.startTime) {
      setUpAnimation();
    }
    
    final double upTime = NSProcessInfo.getSharedProcessInfo().getSystemUptime()*1000;
    final long ctime = (long)upTime - this.delayTime;
    this.currentTime = this.speedScale*(ctime - this.startTime)/1000;

    if (this.manager.hasAnimation()) {
      try {
        this.manager.updateGroupObjects(this.currentTime);
      } catch(ConcurrentModificationException e) {
        //　再生中に再読み込みが何度も押されすぎたら落ちてしまうので、そのエラーをキャッチしています。
        e.getMessage();
      }
    }

    if (this.renderer.isRequiredToCallDisplay()) {
      this.renderer.updateDisplay();
    }

    if (this.currentTime > this.endTime) {
      cancel();
      tearDownAnimation();
    }
  }

  /**
   * アニメーションの現在の時刻を返します。
   * 
   * @return 現在の時刻
   */
  public double getCurrentTime() {
    return this.currentTime;
  }

  /**
   * アニメーションの現在の時刻を設定します。
   * 
   * @param t 現在の時刻
   */
  public void setCurrentTime(double t) {
    this.currentTime = t;
  }

  /**
   * アニメーションの完了を通知します。
   */
  private void tearDownAnimation() {
    for (final AnimationTaskListener listener : this.listeners) {
      listener.tearDownAnimation();
    }
  }

  /**
   * アニメーションの開始を通知します。
   */
  private void setUpAnimation() {
    for (final AnimationTaskListener listener : this.listeners) {
      listener.setUpAnimation();
    }
  }
}
