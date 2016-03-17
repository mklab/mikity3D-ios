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

import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.uikit.UIBarButtonItem;
import org.robovm.apple.uikit.UIBarButtonItem.OnClickListener;
import org.robovm.apple.uikit.UIBarButtonSystemItem;
import org.robovm.apple.uikit.UINavigationController;
import org.robovm.apple.uikit.UIViewController;

/**
 * ナビゲーションバーを表すクラス
 */
public class Navigation extends UINavigationController {
	/** ナビゲーションバーを配置するViewController */
	private Canvas canvas;
	
	public Navigation(Canvas canvas) {
		super(canvas);
		this.canvas = canvas;
	}
	
	public NSArray<UIBarButtonItem> getPlayerButtons() {
		UIBarButtonItem play = new UIBarButtonItem(UIBarButtonSystemItem.Play, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
				Navigation.this.canvas.runAnimation();
			}
		});
		
		UIBarButtonItem stop = new UIBarButtonItem(UIBarButtonSystemItem.Stop, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
				Navigation.this.canvas.stopAnimation();
			}
		});
		
		UIBarButtonItem pause = new UIBarButtonItem(UIBarButtonSystemItem.Pause, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
				Navigation.this.canvas.pauseAnimation();
			}
		});
		
		UIBarButtonItem repeat = new UIBarButtonItem(UIBarButtonSystemItem.Refresh, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
				Navigation.this.canvas.repeatAnimation();
			}
		});
		
		return new NSArray<>(repeat, pause, stop, play);
	}
	
	public UIBarButtonItem getDisplayItem(final UIViewController viewController, final UIBarButtonItem button) {
		UIBarButtonItem display = new UIBarButtonItem(UIBarButtonSystemItem.Compose, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
//				viewController.getNavigationItem().setLeftBarButtonItem(button);
				UINavigationController navi = new UINavigationController(viewController);
				presentViewController(navi, true, null);
			}
		});
		
		return display;
	}
}
