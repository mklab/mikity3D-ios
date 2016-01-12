package org.mklab.mikity.ios;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.uikit.UIModalPresentationStyle;
import org.robovm.apple.uikit.UINavigationController;
import org.robovm.apple.uikit.UIPopoverPresentationController;
import org.robovm.apple.uikit.UIPopoverPresentationControllerDelegate;
import org.robovm.apple.uikit.UIPresentationController;
import org.robovm.apple.uikit.UITraitCollection;
import org.robovm.apple.uikit.UIView.UIViewPtr;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIViewControllerTransitionCoordinator;

public class Navigation extends UINavigationController implements UIPopoverPresentationControllerDelegate{

	public Navigation(UIViewController viewController) {
		super(viewController);
	}

	@Override
	public UIModalPresentationStyle getAdaptivePresentationStyle(UIPresentationController controller) {
		return UIModalPresentationStyle.None;
	}

	@Override
	public UIModalPresentationStyle getAdaptivePresentationStyle(UIPresentationController controller,
			UITraitCollection traitCollection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UIViewController getViewController(UIPresentationController controller, UIModalPresentationStyle style) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void willPresent(UIPresentationController presentationController, UIModalPresentationStyle style,
			UIViewControllerTransitionCoordinator transitionCoordinator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareForPopoverPresentation(UIPopoverPresentationController popoverPresentationController) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldDismissPopover(UIPopoverPresentationController popoverPresentationController) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void didDismissPopover(UIPopoverPresentationController popoverPresentationController) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void willRepositionPopover(UIPopoverPresentationController popoverPresentationController, CGRect rect,
			UIViewPtr view) {
		// TODO Auto-generated method stub
		
	}


}
