package org.mklab.mikity.ios;

import org.mklab.mikity.ios.messenger.CanvasMenuMessenger;
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

/**
 * Mikity3D for iOSのメインクラスを表す
 * 
 * @author eguchi
 */
public class Main extends UIApplicationDelegateAdapter implements GLKViewControllerDelegate, UIGestureRecognizerDelegate {
	/** ウィンドウ */
	private UIWindow window = null;
	
	/** 3Dモデル描画機能を持つViewController */
	private Canvas canvas;
	
	/** サイドメニュー機能を持つViewController */
	private Menu menu;
	
	
	private UIBarButtonItem displayMenu;

	/* (non-Javadoc)
	 * @see org.robovm.apple.uikit.UIApplicationDelegateAdapter#didFinishLaunching(org.robovm.apple.uikit.UIApplication, org.robovm.apple.uikit.UIApplicationLaunchOptions)
	 */
	@Override
	public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
		this.window = new UIWindow(UIScreen.getMainScreen().getBounds());
		EAGLContext context = new EAGLContext(EAGLRenderingAPI.OpenGLES1);
		GLKView view = new GLKView(UIScreen.getMainScreen().getBounds(), context);
		
		CanvasMenuMessenger messenger = new CanvasMenuMessenger();
		
		canvas = new Canvas(messenger);
		canvas.viewDidLoad();
		view.setDelegate(canvas);
		
		menu = new Menu(messenger);
		
		UISplitViewController split = new UISplitViewController() {
			@Override
			public boolean shouldAutorotate() {
				return menu.isRockRotation();
			}
		};
		
		messenger.setCanvas(canvas);
		messenger.setMenu(menu);
		
		canvas.setView(view);
		
		Navigation navi = new Navigation(canvas);
		
	    UIPinchGestureRecognizer pinchGesture = new UIPinchGestureRecognizer();
	    Selector pinchSelector = Selector.register("handlePinch:");
	    pinchGesture.addTarget(this, pinchSelector);
	    pinchGesture.setDelegate(this);
	    canvas.getView().addGestureRecognizer(pinchGesture);
	    
	    UIPanGestureRecognizer panGesture = new UIPanGestureRecognizer();
	    Selector panSelector = Selector.register("handlePan:");
	    panGesture.addTarget(this, panSelector);
	    panGesture.setDelegate(this);
	    canvas.getView().addGestureRecognizer(panGesture);
		
		String modelName = UIDevice.getCurrentDevice().getModel();
		
		if (modelName.equals("iPhone") || modelName.equals("iPod touch")) {
			split.setViewControllers(new NSArray<UIViewController>(navi, menu));
			canvas.getNavigationItem().setLeftBarButtonItem(navi.getDisplayItem(menu, menu.getNavigationItem().getLeftBarButtonItem()));
		} else {
			split.setViewControllers(new NSArray<UIViewController>(menu, navi));
			canvas.getNavigationItem().setLeftBarButtonItem(split.getDisplayModeButtonItem());
		}
		
		split.setPresentsWithGesture(false);
		split.setPreferredDisplayMode(UISplitViewControllerDisplayMode.PrimaryHidden);
		
		canvas.getNavigationItem().setTitle("Mikity3D");
		canvas.getNavigationItem().setRightBarButtonItems(navi.getPlayerButtons());
		
		canvas.setDelegate(this);
		canvas.setPreferredFramesPerSecond(60);
		
		this.window.setRootViewController(split);
		this.window.setBackgroundColor(UIColor.white());
		this.window.makeKeyAndVisible();
		
		UINavigationItem item = split.getNavigationItem();

		return true;
	}

	public static void main(String[] args) {
	    try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
	        UIApplication.main(args, null, Main.class);
	    }
	}

	@Override
	public void update(GLKViewController controller) {
	}

	@Override
	public void willPause(GLKViewController controller, boolean pause) {
	}
	
	@Callback @BindSelector("handlePan:")
	public static void handlePan(Main self, Selector selector, UIPanGestureRecognizer gestureRecognizer) {
		self.canvas.handlePan(gestureRecognizer);
	}
	
	@Callback @BindSelector("handlePinch:")
	public static void handlePinch(Main self, Selector selector, UIPinchGestureRecognizer gestureRecognizer) {
		self.canvas.handlePinch(gestureRecognizer);
	}

	@Override
	public boolean shouldBeRequiredToFail(UIGestureRecognizer gestureRecognizer,
			UIGestureRecognizer otherGestureRecognizer) {
		return false;
	}

	@Override
	public boolean shouldBegin(UIGestureRecognizer gestureRecognizer) {
		return true;
	}

	@Override
	public boolean shouldReceivePress(UIGestureRecognizer gestureRecognizer, UIPress press) {
		return true;
	}

	@Override
	public boolean shouldReceiveTouch(UIGestureRecognizer gestureRecognizer, UITouch touch) {
		return true;
	}

	@Override
	public boolean shouldRecognizeSimultaneously(UIGestureRecognizer gestureRecognizer,
			UIGestureRecognizer otherGestureRecognizer) {
		return false;
	}

	@Override
	public boolean shouldRequireFailure(UIGestureRecognizer gestureRecognizer,
			UIGestureRecognizer otherGestureRecognizer) {
		return false;
	}
}