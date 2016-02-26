package org.mklab.mikity.ios.sample;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;

import org.mklab.mikity.ios.GL;
import org.mklab.mikity.ios.GLU;
import org.mklab.mikity.model.xml.simplexml.model.ColorModel;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.glkit.GLKView;
import org.robovm.apple.glkit.GLKViewController;
import org.robovm.apple.glkit.GLKViewDelegate;
import org.robovm.apple.opengles.EAGLContext;
import org.robovm.apple.uikit.UIScreen;

/**
 * レンダラーを使用する例題クラスです。
 * 
 * @author ohashi
 * @version $Revision$, 2012/10/29
 */
public class GLRendererSample extends GLKViewController implements GLKViewDelegate {
	/** アスペクト比 */
	private float aspect;
	/** 回転角度 */
	private int angle;
	
	// 頂点バッファの生成
	private final float[] vertexs = { 1.0f, 1.0f, 1.0f, // 頂点0
			1.0f, 1.0f, -1.0f, // 頂点1
			-1.0f, 1.0f, 1.0f, // 頂点2
			-1.0f, 1.0f, -1.0f, // 頂点3
			1.0f, -1.0f, 1.0f, // 頂点4
			1.0f, -1.0f, -1.0f, // 頂点5
			-1.0f, -1.0f, 1.0f, // 頂点6
			-1.0f, -1.0f, -1.0f,// 頂点7
	};

	/** 頂点バッファ */
	private FloatBuffer vertexBuffer;
	/** インデックスバッファ */
	private ByteBuffer indexBuffer;
	/** 法線バッファ */
	private FloatBuffer normalBuffer;

	@Override
	public void viewDidLoad() {
		super.viewDidLoad();
		CGRect bounds = UIScreen.getMainScreen().getBounds();
		onSurfaceChanged(null, (int) bounds.getWidth(), (int) bounds.getHeight());
		onSurfaceCreated(null);
	}

	@Override
	public void draw(GLKView view, CGRect rect) {
		onDrawFrame(null);
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSurfaceCreated(GL gl) {
		// 画面のクリア
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		// 光源の有効化
		GL.glEnable(GL.GL_LIGHTING);

		// カラーマテリアルの有効化
		GL.glEnable(GL.GL_COLOR_MATERIAL);

		GL.glEnable(GL.GL_NORMALIZE);
		GL.glEnable(GL.GL_LIGHT0);
		GL.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 100.0f);

		// 光源色の指定
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[] { 0.0f, 0.1f, 0.1f, 0.1f }, 0); // 平行光源を設定します
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, new float[] { 0.0f, 0.0f, 0.0f, 1.0f }, 0);
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, new float[] { 0.7f, 0.7f, 0.7f, 1.0f }, 0);
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, new float[] { 0.2f, 0.2f, 0.2f, 1.0f }, 0);

		this.vertexBuffer = makeFloatBuffer(vertexs);

		// インデックスバッファの生成
		byte[] indexs = { 0, 1, 2, 3, 6, 7, 4, 5, 0, 1, // 面0
				1, 5, 3, 7, // 面1
				0, 2, 4, 6, // 面2
		};
		this.indexBuffer = makeByteBuffer(indexs);

		// 法線バッファの生成
		float[] normals = { 1.0f, 1.0f, 1.0f, // 頂点0
				1.0f, 1.0f, -1.0f, // 頂点1
				-1.0f, 1.0f, 1.0f, // 頂点2
				-1.0f, 1.0f, -1.0f, // 頂点3
				1.0f, -1.0f, 1.0f, // 頂点4
				1.0f, -1.0f, -1.0f, // 頂点5
				-1.0f, -1.0f, 1.0f, // 頂点6
				-1.0f, -1.0f, -1.0f,// 頂点7
		};
		float div = (float) Math.sqrt((1.0f * 1.0f) + (1.0f * 1.0f) + (1.0f * 1.0f));
		for (int i = 0; i < normals.length; i++) {
			normals[i] /= div;
		}
		this.normalBuffer = makeFloatBuffer(normals);
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSurfaceChanged(GL gl, int w, int h) {
		// ビューポート変換
		GL.glViewport(0, 0, w, h);
		this.aspect = (float) w / (float) h;
	}

	/**
	 * {@inheritDoc}
	 */
	public void onDrawFrame(GL gl) {
		// 画面のクリア
		GL.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		// 射影変換
		GL.glMatrixMode(GL.GL_PROJECTION);
		GL.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, // Y方向の画角
				this.aspect, // アスペクト比
				0.01f, // ニアクリップ
				100.0f);// ファークリップ

		// デプステストの有効化
		GL.glEnable(GL.GL_DEPTH_TEST);

		// 光源位置の指定
		GL.glMatrixMode(GL.GL_MODELVIEW);
		GL.glLoadIdentity();
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[] { 2.5f, 2.5f, 0.0f, 1.0f }, 0);

		// ビュー変換
		GLU.gluLookAt(gl, 0.0f, 5.0f, 5.0f, // カメラの視点
				0.0f, 0.0f, 0.0f, // カメラの焦点
				0.0f, 1.0f, 0.0f);// カメラの上方向

		// モデル変換
		GL.glRotatef(this.angle++, 0, 1, 0);

		// ボックスの描画
		drawBox(gl);
	}

	/**
	 * ボックスを描画します。
	 * 
	 * @param gl
	 */
	private void drawBox(GL gl) {
		GL.glEnable(GL.GL_BLEND); // ブレンドを有効にします
		GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		// 面0の描画
		applyColor(0, 0, 1);
//		setMaterial(gl, 0, 0, 1, 1);
		
		// 法線バッファの指定
		GL.glEnableClientState(GL.GL_NORMAL_ARRAY);
		GL.glNormalPointer(GL.GL_FLOAT, 0, this.normalBuffer);
		
		// 頂点バッファの指定
		GL.glEnableClientState(GL.GL_VERTEX_ARRAY);
		GL.glVertexPointer(3, GL.GL_FLOAT, 0, this.vertexBuffer);
		
		final int number = this.vertexs.length / 3;
		GL.glDrawArrays(GL.GL_TRIANGLES, 0, number);
		
//		this.indexBuffer.position(0);
//		GL.glDrawElements(GL.GL_TRIANGLE_STRIP, 10, GL.GL_UNSIGNED_BYTE, this.indexBuffer);
//
//		// 面1の描画
//		setMaterial(gl, 1, 1, 0, 1);
//		this.indexBuffer.position(10);
//		GL.glDrawElements(GL.GL_TRIANGLE_STRIP, 4, GL.GL_UNSIGNED_BYTE, this.indexBuffer);
//
//		// 面2の描画
//		setMaterial(gl, 1, 1, 0, 1);
//		this.indexBuffer.position(14);
//		GL.glDrawElements(GL.GL_TRIANGLE_STRIP, 4, GL.GL_UNSIGNED_BYTE, this.indexBuffer);

	}

	/**
	 * マテリアルを設定します。
	 * 
	 * @param gl
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	private static void setMaterial(GL gl, float r, float g, float b, float a) {
		// マテリアルの環境光色の指定
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, new float[] { r, g, b, a }, 0);

		// マテリアルの拡散光色の指定
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, new float[] { r, g, b, a }, 0);

		// マテリアルの鏡面光色と鏡面指数の指定
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, new float[] { r, g, b, a }, 0);
		GL.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, 0.6f);
	}

	/**
	 * 色を適用します。
	 * 
	 * @param gl
	 *            GL
	 */
	private void applyColor(float r, float g, float b) {
		// final ColorModel color =
		// ((AbstractGraphicObject)this.object).getColor();
		GL.glColor4f(r, g, b, 1.0f);

	}

	/**
	 * float配列をFloatBufferに変換します。
	 * 
	 * @param array
	 *            変換対象
	 * @return 変換結果
	 */
	private static FloatBuffer makeFloatBuffer(float[] array) {
		final FloatBuffer buffer = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		buffer.put(array).position(0);
		return buffer;
	}

	/**
	 * byte配列をByteBufferに変換します。
	 * 
	 * @param array
	 *            変換対象
	 * @return 変換結果
	 */
	private static ByteBuffer makeByteBuffer(byte[] array) {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		buffer.put(array).position(0);
		return buffer;
	}
}