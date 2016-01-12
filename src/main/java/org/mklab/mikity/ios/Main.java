package org.mklab.mikity.ios;

import org.mklab.mikity.ios.sample.GLRendererSample;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.glkit.GLKView;
import org.robovm.apple.glkit.GLKViewController;
import org.robovm.apple.glkit.GLKViewControllerDelegate;
import org.robovm.apple.glkit.GLKViewDelegate;
import org.robovm.apple.opengles.EAGLContext;
import org.robovm.apple.opengles.EAGLRenderingAPI;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationDelegateAdapter;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIButtonType;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControl;
import org.robovm.apple.uikit.UIControlState;
import org.robovm.apple.uikit.UIEvent;
import org.robovm.apple.uikit.UIFont;
import org.robovm.apple.uikit.UIModalPresentationStyle;
import org.robovm.apple.uikit.UINavigationController;
import org.robovm.apple.uikit.UIPopoverArrowDirection;
import org.robovm.apple.uikit.UIPopoverPresentationController;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UISplitViewController;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIWindow;

public class Main extends UIApplicationDelegateAdapter implements GLKViewControllerDelegate {
	private UIWindow window = null;
	private boolean increasing = false;
	private float curRed = 0.0f;

	@Override
	public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
		this.window = new UIWindow(UIScreen.getMainScreen().getBounds());
		EAGLContext context = new EAGLContext(EAGLRenderingAPI.OpenGLES1);
		GLKView view = new GLKView(UIScreen.getMainScreen().getBounds(), context);
		
		GLRendererSample viewController = new GLRendererSample();
		viewController.viewDidLoad();
		view.setDelegate(viewController);
		
		UISplitViewController split = new UISplitViewController();
		
		MyViewController secontView = new MyViewController();
		
		viewController.setView(view);
		
		final Navigation navi = new Navigation(viewController);
		
		split.setViewControllers(new NSArray<UIViewController>(secontView, navi));
		
		final Menu menu = new Menu();
		menu.setModalPresentationStyle(UIModalPresentationStyle.Popover);
		menu.setPreferredContentSize(new CGSize(100, 100));
		
		navi.addChildViewController(menu);
		
		final UIButton button = new UIButton(UIButtonType.RoundedRect);
		button.setFrame(new CGRect(0, 0, 100, 100));
		button.setTitle("check", UIControlState.Normal);
		button.getTitleLabel().setFont(UIFont.getBoldSystemFont(22));
		button.addOnTouchUpInsideListener(new UIControl.OnTouchUpInsideListener() {
			
			@Override
			public void onTouchUpInside(UIControl control, UIEvent event) {
				navi.presentViewController(menu, true, null);
				
				UIPopoverPresentationController popover = menu.getPopoverPresentationController();
				popover.setPermittedArrowDirections(UIPopoverArrowDirection.Left);
				popover.setDelegate(navi);
				popover.setBackgroundColor(UIColor.cyan());
				popover.setSourceView(button);
				popover.setSourceRect(new CGRect(0, 0, button.getFrame().getWidth(), button.getFrame().getHeight()));
			}
		});
		navi.getNavigationBar().addSubview(button);
		
//		viewController.getNavigationItem().setLeftBarButtonItem(split.getDisplayModeButtonItem());
		
		viewController.setDelegate(this);
		viewController.setPreferredFramesPerSecond(60);
		this.window.setRootViewController(navi);
		
		this.window.setBackgroundColor(UIColor.white());
		this.window.makeKeyAndVisible();

		return true;
	}

	@Override
	public void update(GLKViewController controller) {
		if (increasing) {
			curRed += 1.0f * controller.getTimeSinceLastUpdate();
		} else {
			curRed -= 1.0f * controller.getTimeSinceLastUpdate();
		}
		if (curRed >= 1.0f) {
			curRed = 1.0f;
			increasing = false;
		}
		if (curRed <= 0.0f) {
			curRed = 0.0f;
			increasing = true;
		}
	}

    public static void main(String[] args) {
        try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
            UIApplication.main(args, null, Main.class);
        }
    }

	@Override
	public void willPause(GLKViewController controller, boolean pause) {
	}
}
