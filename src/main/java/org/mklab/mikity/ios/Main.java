package org.mklab.mikity.ios;

import org.mklab.mikity.ios.messenger.CanvasMenuInterface;
import org.mklab.mikity.ios.viewcontroller.Canvas;
import org.mklab.mikity.ios.viewcontroller.Menu;
import org.mklab.mikity.ios.viewcontroller.Navigation;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.glkit.GLKView;
import org.robovm.apple.glkit.GLKViewController;
import org.robovm.apple.glkit.GLKViewControllerDelegate;
import org.robovm.apple.opengles.EAGLContext;
import org.robovm.apple.opengles.EAGLRenderingAPI;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationDelegateAdapter;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIBarButtonItem;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIGestureRecognizer;
import org.robovm.apple.uikit.UIGestureRecognizerDelegate;
import org.robovm.apple.uikit.UIModalPresentationStyle;
import org.robovm.apple.uikit.UINavigationItem;
import org.robovm.apple.uikit.UIPanGestureRecognizer;
import org.robovm.apple.uikit.UIPinchGestureRecognizer;
import org.robovm.apple.uikit.UIPress;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UISplitViewController;
import org.robovm.apple.uikit.UISplitViewControllerDisplayMode;
import org.robovm.apple.uikit.UITouch;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIWindow;
import org.robovm.objc.Selector;
import org.robovm.objc.annotation.BindSelector;
import org.robovm.rt.bro.annotation.Callback;

public class Main extends UIApplicationDelegateAdapter implements GLKViewControllerDelegate, UIGestureRecognizerDelegate {
	private UIWindow window = null;
	
	private Canvas viewController;
	private Menu menu;
	private UIBarButtonItem displayMenu;

	@Override
	public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
		this.window = new UIWindow(UIScreen.getMainScreen().getBounds());
		EAGLContext context = new EAGLContext(EAGLRenderingAPI.OpenGLES1);
		GLKView view = new GLKView(UIScreen.getMainScreen().getBounds(), context);
		
		CanvasMenuInterface messenger = new CanvasMenuInterface();
		
		viewController = new Canvas(messenger);
		viewController.viewDidLoad();
		view.setDelegate(viewController);
		
		menu = new Menu(messenger);
		
		UISplitViewController split = new UISplitViewController() {
			@Override
			public boolean shouldAutorotate() {
				return menu.isRockRotation();
			}
		};
		
		messenger.setCanvas(viewController);
		messenger.setMenu(menu);
		
		viewController.setView(view);
		
		Navigation navi = new Navigation(viewController);
		
	    UIPinchGestureRecognizer pinchGesture = new UIPinchGestureRecognizer();
	    Selector pinchSelector = Selector.register("handlePinch:");
	    pinchGesture.addTarget(this, pinchSelector);
	    pinchGesture.setDelegate(this);
	    viewController.getView().addGestureRecognizer(pinchGesture);
	    
	    UIPanGestureRecognizer panGesture = new UIPanGestureRecognizer();
	    Selector panSelector = Selector.register("handlePan:");
	    panGesture.addTarget(this, panSelector);
	    panGesture.setDelegate(this);
	    viewController.getView().addGestureRecognizer(panGesture);
		
		String modelName = UIDevice.getCurrentDevice().getModel();
		
		if (modelName.equals("iPhone") || modelName.equals("iPod touch")) {
			split.setViewControllers(new NSArray<UIViewController>(navi, menu));
			viewController.getNavigationItem().setLeftBarButtonItem(navi.getDisplayItem(menu, menu.getNavigationItem().getLeftBarButtonItem()));
		} else {
			split.setViewControllers(new NSArray<UIViewController>(menu, navi));
			viewController.getNavigationItem().setLeftBarButtonItem(split.getDisplayModeButtonItem());
		}
		
		split.setPresentsWithGesture(false);
		split.setPreferredDisplayMode(UISplitViewControllerDisplayMode.PrimaryHidden);
		
		viewController.getNavigationItem().setTitle("Mikity3D");
		viewController.getNavigationItem().setRightBarButtonItems(navi.getPlayerButtons());
		
		viewController.setDelegate(this);
		viewController.setPreferredFramesPerSecond(60);
		
		this.window.setRootViewController(split);
		this.window.setBackgroundColor(UIColor.white());
		this.window.makeKeyAndVisible();
		
		UINavigationItem item = split.getNavigationItem();

		return true;
	}

	@Override
	public void update(GLKViewController controller) {
	}

    public static void main(String[] args) {
        try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
            UIApplication.main(args, null, Main.class);
        }
    }

	@Override
	public void willPause(GLKViewController controller, boolean pause) {
	}
	
	@Callback @BindSelector("handlePan:")
	public static void handlePan(Main self, Selector selector, UIPanGestureRecognizer gestureRecognizer) {
		self.viewController.handlePan(gestureRecognizer);
	}
	
	@Callback @BindSelector("handlePinch:")
	public static void handlePinch(Main self, Selector selector, UIPinchGestureRecognizer gestureRecognizer) {
		self.viewController.handlePinch(gestureRecognizer);
	}

	@Override
	public boolean shouldBeRequiredToFail(UIGestureRecognizer gestureRecognizer,
			UIGestureRecognizer otherGestureRecognizer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldBegin(UIGestureRecognizer gestureRecognizer) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldReceivePress(UIGestureRecognizer gestureRecognizer, UIPress press) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldReceiveTouch(UIGestureRecognizer gestureRecognizer, UITouch touch) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldRecognizeSimultaneously(UIGestureRecognizer gestureRecognizer,
			UIGestureRecognizer otherGestureRecognizer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldRequireFailure(UIGestureRecognizer gestureRecognizer,
			UIGestureRecognizer otherGestureRecognizer) {
		// TODO Auto-generated method stub
		return false;
	}

}