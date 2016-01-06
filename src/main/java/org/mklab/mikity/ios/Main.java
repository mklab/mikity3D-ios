package org.mklab.mikity.ios;

import org.mklab.mikity.ios.sample.GLRendererSample;
import org.robovm.apple.coregraphics.CGRect;
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
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UINavigationController;
import org.robovm.apple.uikit.UIScreen;
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
		
		UINavigationController navi = new UINavigationController(viewController);

		viewController.setView(view);
		
//		final MyGLKViewControllerDelegate gcDelegate = new MyGLKViewControllerDelegate(this);
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
