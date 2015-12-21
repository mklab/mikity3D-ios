package org.mklab.mikity.ios.sample;

import org.mklab.mikity.ios.view.renderer.opengles.OpenglesObjectRenderer;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.glkit.GLKView;
import org.robovm.apple.opengles.EAGLContext;

/**
 * @author ohashi
 * @version $Revision$, 2013/02/07
 */
public class GLViewSample extends GLKView {
	private OpenglesObjectRenderer myRenderer;

	/**
	 * @param rect
	 * @param context
	 */
	public GLViewSample(CGRect rect, EAGLContext context) {
		super(rect, context);
		// setRenderer(this.myRenderer);
	}
	
	@Override
	protected long init() {
		return 0;
		
	}

}
