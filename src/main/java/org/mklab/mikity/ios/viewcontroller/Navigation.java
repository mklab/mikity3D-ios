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
