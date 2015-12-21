/*
 * Created on 2015/12/12
 * Copyright (C) 2015 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.mikity.ios.view.renderer.opengles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.mklab.mikity.ios.GL;

import org.mklab.mikity.model.graphic.GridObject;
import org.mklab.mikity.model.xml.simplexml.ConfigurationModel;
import org.mklab.mikity.model.xml.simplexml.model.ColorModel;

/**
 * Openglesのグリッドを表すクラスです。
 * 
 * @author koga
 * @version $Revision$, 2015/12/12
 */
public class OpenglesGridObject {
  /** 環境。 */
  private ConfigurationModel configuration;
  
  /** グリッドオブジェクト。 */
  private GridObject grid;

  /**
   * 新しく生成された<code>JoglGridObject</code>オブジェクトを初期化します。
   * @param configuration 環境
   */
  public OpenglesGridObject(ConfigurationModel configuration) {
    this.configuration = configuration;
    this.grid = new GridObject(configuration.getBaseCoordinate());
  }
  
  /**
   * 環境データを設定します。
   * 
   * @param configuration 環境データ
   */
  void setConfiguration(ConfigurationModel configuration) {
    this.configuration = configuration;
  }
  
  /**
   * オブジェクトを表示します。
   * 
   * @param gl GL
   */
  public void display(GL gl) {
    final boolean isAxisShowing = this.configuration.getBaseCoordinate().isAxisShowing();
    if (isAxisShowing) {
      drawBaseAxis(gl);
    }
    
    final boolean isGridShowing = this.configuration.getBaseCoordinate().isGridShowing();
    if (isGridShowing) {
      drawGrid(gl);
    }
  }
  
  /**
   * 色を適用します。
   * 
   * @param gl GL　
   */
  private void applyColor(GL gl, ColorModel color) {
    gl.glColor4f(color.getRf(), color.getGf(), color.getBf(), color.getAlphaf());
  }
  
  /**
   * 絶対座標の座標軸を描画します。
   * 
   * @param gl GL
   */
  private void drawBaseAxis(GL gl) {
    final float axisMin = -10;
    final float axisMax = 10;
    
    // x軸の描画
    applyColor(gl, new ColorModel("red")); //$NON-NLS-1$
    float[] xAxis = {axisMin, 0, 0, axisMax, 0, 0};
    drawLines(gl, xAxis);

    // y軸の描画
    applyColor(gl, new ColorModel("blue")); //$NON-NLS-1$
    float[] yAxis = {0, axisMin, 0, 0, axisMax, 0};
    drawLines(gl, yAxis);

    // z軸の描画
    applyColor(gl, new ColorModel("green")); //$NON-NLS-1$
    float[] zAxis = {0, 0, axisMin, 0, 0, axisMax}; 
    drawLines(gl, zAxis);
  }
  
  /**
   *  線分を描画します。
   *  
   * @param gl GL
   * @param vertexArray 頂点の成分配列
   */
  private void drawLines(GL gl, final float[] vertexArray) {
    final FloatBuffer vertexBuffer = makeFloatBuffer(vertexArray);
    
    // 頂点配列の有効化
    gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
    // 頂点バッファの指定
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer);

    final int number = vertexArray.length/3;
    gl.glDrawArrays(GL.GL_LINES, 0, number);
  }
  
  /**
   * float配列をFloatBufferに変換します。
   * 
   * @param array 変換元
   * @return 変換結果
   */
  private FloatBuffer makeFloatBuffer(float[] array) {
    final FloatBuffer buffer = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    buffer.put(array).position(0);
    return buffer;
  }

  /**
   * グリッドを描画します。
   * 
   * @param gl GL
   */
  private void drawGrid(GL gl) {
    applyColor(gl, this.configuration.getBaseCoordinate().getGridColor());
    drawLines(gl, this.grid.getVertexArray());
  }

}
