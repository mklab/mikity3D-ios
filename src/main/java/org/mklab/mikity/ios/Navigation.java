package org.mklab.mikity.ios;

import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.uikit.UIBarButtonItem;
import org.robovm.apple.uikit.UIBarButtonItem.OnClickListener;
import org.robovm.apple.uikit.UIBarButtonItemGroup;
import org.robovm.apple.uikit.UIBarButtonItemStyle;
import org.robovm.apple.uikit.UIBarButtonSystemItem;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UINavigationController;
import org.robovm.apple.uikit.UIViewController;

/**
 * ナビゲーションバーを表すクラス
 */
public class Navigation extends UINavigationController {
	public Navigation(UIViewController controller) {
		super(controller);
		
	}
	
	public NSArray<UIBarButtonItem> getPlayerButtons() {
		UIBarButtonItem play = new UIBarButtonItem(UIBarButtonSystemItem.Play, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
				// TODO Auto-generated method stub	
			}
		});
		
		UIBarButtonItem stop = new UIBarButtonItem(UIBarButtonSystemItem.Stop, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
				// TODO Auto-generated method stub	
			}
		});
		
		UIBarButtonItem pause = new UIBarButtonItem(UIBarButtonSystemItem.Pause, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
				// TODO Auto-generated method stub
			}
		});
		
		UIBarButtonItem repeat = new UIBarButtonItem(UIBarButtonSystemItem.Refresh, new OnClickListener() {
			@Override
			public void onClick(UIBarButtonItem barButtonItem) {
				// TODO Auto-generated method stub	
			}
		});
		
		return new NSArray<>(repeat, pause, stop, play);
	}
}
