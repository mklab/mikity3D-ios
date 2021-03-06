/**
 * Copyright (C) 2015 MKLab.org (Koga Laboratory)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mklab.mikity.ios.viewcontroller;
import java.io.File;

import org.mklab.mikity.ios.DirectoryPath;
import org.mklab.mikity.ios.messenger.CanvasMenuMessenger;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSFileManager;
import org.robovm.apple.foundation.NSSearchPathDirectory;
import org.robovm.apple.foundation.NSSearchPathDomainMask;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UIBarButtonItem;
import org.robovm.apple.uikit.UIBarButtonSystemItem;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIButtonType;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControl;
import org.robovm.apple.uikit.UIControl.OnTouchUpInsideListener;
import org.robovm.apple.uikit.UIControl.OnValueChangedListener;
import org.robovm.apple.uikit.UIControlState;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIEvent;
import org.robovm.apple.uikit.UIFont;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UIModalPresentationStyle;
import org.robovm.apple.uikit.UIModalTransitionStyle;
import org.robovm.apple.uikit.UIScrollView;
import org.robovm.apple.uikit.UISwitch;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIBarButtonItem.OnClickListener;

/**
 * メニュー画面を表すクラス
 */
public class Menu extends UIViewController{
	private DirectoryPath resourcePath;
	
	private UILabel sampleSourceLabel;
	private UILabel sampleModelLabel;
	
	/** スイッチ */
//	private UISwitch accelerometerSwitch;
	private UISwitch rockRotationSwitch;
	private UISwitch gridShowingSwitch;
	private UISwitch axisShowingSwitch;
	
	private CanvasMenuMessenger messenger;
	
	public Menu(CanvasMenuMessenger messenger) {
		UIScrollView scrollView = new UIScrollView();
		scrollView.setFrame(new CGRect(0, 0, 300, 100));
		scrollView.setContentSize(new CGSize(300, 1000));
		scrollView.setBackgroundColor(UIColor.white());
		setView(scrollView);
		
		this.resourcePath = new DirectoryPath("sample");
		this.messenger = messenger;
		
		// play speed
		UILabel playSpeed = createLabel("Play Speed	" + 1.00, new CGRect(10, 10, 300, 30));
		UIButton backPlayButton = createButton("<<", new CGRect(100, 50, 50, 30));
		UIButton fastPlayButton = createButton(">>", new CGRect(160, 50, 50, 30));
		scrollView.addSubview(playSpeed);
		scrollView.addSubview(backPlayButton);
		scrollView.addSubview(fastPlayButton);
		
		// model button
		UIButton modelButton = createButton("Model", new CGRect(10, 100, 70, 30));
		scrollView.addSubview(modelButton);
		
		// model edit button
		UIButton modelEditButton = createButton("Model Edit", new CGRect(10, 150, 120, 30));
		scrollView.addSubview(modelEditButton);
		
		// sensor switches
		UILabel slope = createLabel("slope sensor", new CGRect(10, 190, 150, 30));
		UISwitch slopeSwitch = new UISwitch(new CGRect(170, 190, 50, 30));
		scrollView.addSubview(slope);
		scrollView.addSubview(slopeSwitch);
		
		UILabel accelerometer = createLabel("accel sensor", new CGRect(10, 230, 150, 30));
		UISwitch accelerometerSwitch = new UISwitch(new CGRect(170, 230, 50, 30));
		scrollView.addSubview(accelerometer);
		scrollView.addSubview(accelerometerSwitch);
		
		UILabel rockRotation = createLabel("rotation rock", new CGRect(10, 270, 150, 30));
		this.rockRotationSwitch = new UISwitch(new CGRect(170, 270, 50, 30));
		rockRotationSwitch.setOn(shouldAutorotate());
		scrollView.addSubview(rockRotation);
		scrollView.addSubview(rockRotationSwitch);
		
		UILabel gridShowing = createLabel("grid showing", new CGRect(10, 310, 150, 30));
		this.gridShowingSwitch = new UISwitch(new CGRect(170, 310, 50, 30));
		this.gridShowingSwitch.setOn(false);
		this.gridShowingSwitch.addOnValueChangedListener(new OnValueChangedListener() {
			@Override
			public void onValueChanged(UIControl control) {
				Menu.this.messenger.setGridShowing(Menu.this.gridShowingSwitch.isOn());
			}
		});
		scrollView.addSubview(gridShowing);
		scrollView.addSubview(this.gridShowingSwitch);
		
		UILabel axisShowing = createLabel("axis showing", new CGRect(10, 350, 150, 30));
		this.axisShowingSwitch = new UISwitch(new CGRect(170, 350, 50, 30));
		this.axisShowingSwitch.setOn(false);
		this.axisShowingSwitch.addOnValueChangedListener(new OnValueChangedListener() {
			@Override
			public void onValueChanged(UIControl control) {
				Menu.this.messenger.setAxisShowing(Menu.this.axisShowingSwitch.isOn());
			}
		});
		scrollView.addSubview(axisShowing);
		scrollView.addSubview(this.axisShowingSwitch);
		
		// sample
		UILabel sampleLabel = createLabel("Samples", new CGRect(10, 410, 100, 30));
		UIButton sampleModelButton = createButton("Model", new CGRect(10, 450, 70, 30));
		sampleModelButton.addOnTouchUpInsideListener(new OnTouchUpInsideListener() {
			@Override
			public void onTouchUpInside(UIControl control, UIEvent event) {
				SampleMenu sample = new SampleMenu(Menu.this, SampleMenu.MODEL, Menu.this.resourcePath);
				
				sample.getView().setFrame(new CGRect(0,0,300,getView().getFrame().getHeight()));
				
				sample.setModalPresentationStyle(UIModalPresentationStyle.OverCurrentContext);
				sample.setModalTransitionStyle(UIModalTransitionStyle.CoverVertical);
				presentViewController(sample, true, null);
			}
		});
		sampleModelLabel = createLabel("", new CGRect(90, 450, 200, 30));
		
		UIButton sampleSourceButton = createButton("Source", new CGRect(10, 490, 80, 30));
		sampleSourceButton.addOnTouchUpInsideListener(new OnTouchUpInsideListener() {
			@Override
			public void onTouchUpInside(UIControl control, UIEvent event) {
				SampleMenu sample = new SampleMenu(Menu.this, SampleMenu.SOURCE, Menu.this.resourcePath);
				
				sample.getView().setFrame(new CGRect(0,0,300, getView().getFrame().getHeight()));
				
				sample.setModalPresentationStyle(UIModalPresentationStyle.OverCurrentContext);
				sample.setModalTransitionStyle(UIModalTransitionStyle.CoverVertical);
				presentViewController(sample, true, null);
			}
		});
		sampleSourceLabel = createLabel("", new CGRect(100, 490, 200, 30));
		
		scrollView.addSubview(sampleLabel);
		scrollView.addSubview(sampleModelButton);
		scrollView.addSubview(sampleModelLabel);
		scrollView.addSubview(sampleSourceButton);
		scrollView.addSubview(sampleSourceLabel);
	}
	
	public boolean isRockRotation() {
		return this.rockRotationSwitch.isOn();
	}
	
	/**
	 * モデルを設定する
	 * 
	 * @param selectFile 設定するモデルファイル
	 */
	public void setModel(String selectFile) {
		setModelLabel(selectFile);
		
		this.messenger.readModel(this.resourcePath, selectFile);
	}
	
	/**
	 * ソースを設定する
	 * 
	 * @param selectFile 設定するソースファイル
	 */
	public void setSource(String selectFile) {
		setSourceLabel(selectFile);
		
		this.messenger.readSource(this.resourcePath, selectFile);
		
	}
	
	/**
	 * 座標軸表示を設定する
	 * 
	 * @param isShowing 表示フラグ
	 */
	public void setAxisShowing(boolean isShowing) {
		this.axisShowingSwitch.setOn(isShowing);
	}
	
	
	/**
	 * グリッド表示を設定する
	 * 
	 * @param isShowing 表示フラグ
	 */
	public void setGridShowing(boolean isShowing) {
		this.gridShowingSwitch.setOn(isShowing);
	}
	
	/**
	 * 選択モデルを表示する
	 * 
	 * @param model 選択モデル
	 */
	private void setModelLabel(String model) {
		if (this.sampleModelLabel == null) {
			this.sampleModelLabel = createLabel(model, new CGRect(90, 450, 200, 30));
		} else {
			this.sampleModelLabel.setText(model);
		}
	}

	/**
	 * 選択ソースを表示する
	 * 
	 * @param source 選択ソース
	 */
	private void setSourceLabel(String source) {
		if (this.sampleSourceLabel == null) {
			this.sampleSourceLabel = createLabel(source, new CGRect(90, 490, 200, 30));
		} else {
			this.sampleSourceLabel.setText(source);
		}
	}
	
	/**
	 * ラベルを作成する
	 * 
	 * @param text ラベルに表示する文字
	 * @param frame ラベルの位置及びサイズ
	 * @return 作成されたラベル
	 */
	private UILabel createLabel(String text, CGRect frame) {
		UILabel label = new UILabel(frame);
		
		label.setFont(UIFont.getBoldSystemFont(22));
		label.setTextColor(UIColor.black());
		label.setText(text);
		
		return label;
	}
		
	/**
	 * ボタンを作成する
	 * 
	 * @param title ボタン名
	 * @param frame ボタンの位置及びサイズ
	 * @return 作成されたボタン
	 */
	private UIButton createButton(String title, CGRect frame) {
		UIButton button = new UIButton(UIButtonType.RoundedRect);
		button.setFrame(frame);
		button.setTitle(title, UIControlState.Normal);
		button.setBackgroundColor(UIColor.black());
		button.setTitleColor(UIColor.white(), UIControlState.Normal);
		if (button.getState() == UIControlState.Disabled) {
			button.setBackgroundColor(UIColor.gray());
		}
		button.getTitleLabel().setFont(UIFont.getBoldSystemFont(22));

		return button;
	}
	
	/**
	 * 表示ボタンを生成する
	 * 
	 * @param viewController 表示したいViewContrller
	 * @return 表示ボタン
	 */
	public UIBarButtonItem getDisplayItem(final UIViewController viewController) {
		UIBarButtonItem display = new UIBarButtonItem(UIBarButtonSystemItem.Compose, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
				presentViewController(viewController, true, null);
			}
		});
		
		return display;
	}
}
