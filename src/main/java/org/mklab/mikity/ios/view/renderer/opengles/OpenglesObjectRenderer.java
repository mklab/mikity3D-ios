package org.mklab.mikity.ios.view.renderer.opengles;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.mklab.mikity.ios.GL;
import org.mklab.mikity.model.GroupObjectManager;
import org.mklab.mikity.model.xml.simplexml.ConfigurationModel;
import org.mklab.mikity.model.xml.simplexml.config.EyeModel;
import org.mklab.mikity.model.xml.simplexml.config.LightModel;
import org.mklab.mikity.model.xml.simplexml.config.LookAtPointModel;
import org.mklab.mikity.model.xml.simplexml.model.ColorModel;
import org.mklab.mikity.model.xml.simplexml.model.GroupModel;
import org.mklab.mikity.renderer.ObjectRenderer;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.glkit.GLKView;
import org.robovm.apple.glkit.GLKViewController;
import org.robovm.apple.glkit.GLKViewDelegate;
import org.robovm.apple.uikit.UIScreen;

import android.api.GLU;

/**
 * OpenGL用のキャンバスを表すクラスです。
 * 
 * @author ohashi
 * @version $Revision$, 2013/02/06
 */
public class OpenglesObjectRenderer implements ObjectRenderer {
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 5653656698891675370L;

  /** ルートオブジェクト。 */
  private List<OpenglesGroupObject> rootObjects;

  /** 設定。 */
  private ConfigurationModel configuration;
  
  /** グリッド。 */
  private OpenglesGridObject grid; 

  /** アスペクト比 。 */
  private float aspect;
  
  /** Y軸周りの回転角度[rad] */
  private float rotationY = 0.0f;
  /** Z軸周りの回転角度[rad] */
  private float rotationZ = 0.0f;
  /** Y軸方向への移動距離[m] */
  private float translationY = 0.0f;
  /** Z軸方向への移動距離[m] */
  private float translationZ = 0.0f;
  /** 拡大縮小比。 */
  private float scale = 1.0F;

  /** 反射光の強さ */
  private float[] lightSpecular = {0.9f, 0.9f, 0.9f, 1.0f};
  /** 拡散光の強さ */
  private float[] lightDiffuse = {0.5f, 0.5f, 0.5f, 1.0f};
  /** 環境光の強さ */
  private float[] lightAmbient = {0.2f, 0.2f, 0.2f, 1.0f};
  
  private GLKView glkView;
  
  private static IntBuffer depthBuffer = IntBuffer.allocate(1);
  private static IntBuffer colorBuffer = IntBuffer.allocate(1);
  private static IntBuffer frameBuffer = IntBuffer.allocate(1);

  /**
   * 新しく生成された<code>OpenglesModelRenderer</code>オブジェクトを初期化します。
   * 
   * @param glkView GLサーフェースビュー
   * @param configuration 環境 
   */
  public OpenglesObjectRenderer(GLKView glkView, ConfigurationModel configuration) {
    this.glkView = glkView;
    this.configuration = configuration;
    
    this.grid = new OpenglesGridObject(configuration);
  }

  /**
   * {@inheritDoc}
   */
  public void onSurfaceCreated(GL gl) {
    GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

    GL.glEnable(GL.GL_LIGHTING); //光源を有効にします 
    GL.glEnable(GL.GL_COLOR_MATERIAL); //カラーマテリアルを有効にします
    GL.glEnable(GL.GL_NORMALIZE);
    GL.glEnable(GL.GL_LIGHT0); //0番のライトを有効にします
    GL.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 100.0f);
    
  }

  /**
   * {@inheritDoc}
   */
  public void onDrawFrame(GL gl) {
	CGRect bounds = UIScreen.getMainScreen().getBounds();
	createDepthBuffer((int)bounds.getWidth()*2, (int)bounds.getHeight()*2);
//	createColorBuffer((int)bounds.getWidth()*2, (int)bounds.getHeight()*2);

    GL.glEnable(GL.GL_LIGHT0); //0番のライトを有効にします
	GL.glEnable(GL.GL_LIGHTING); //光源を有効にします 
    GL.glEnable(GL.GL_COLOR_MATERIAL); //カラーマテリアルを有効にします
    GL.glEnable(GL.GL_NORMALIZE);
    GL.glEnable(GL.GL_CULL_FACE); // 裏返ったポリゴンを描画しません 

    final ColorModel background = this.configuration.getBackground().getColor();
    GL.glClearColor(background.getRf(), background.getGf(), background.getBf(), background.getAlphaf());  
    GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    GL.glMatrixMode(GL.GL_PROJECTION);
    GL.glLoadIdentity();
    GLU.gluPerspective(gl, 10.0f, this.aspect, 0.1f, 1000.0f);
    
    // 光源位置の指定
    GL.glMatrixMode(GL.GL_MODELVIEW);
    GL.glLoadIdentity(); 
    
    final LightModel light = this.configuration.getLight();
    final float[] lightLocation = new float[]{light.getX(), light.getY(), light.getZ(), 1.0f};
    GL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightLocation, 0); // 平行光源を設定します 
    GL.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, this.lightSpecular, 0); // 反射光の強さを設定します 
    GL.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, this.lightDiffuse, 0); // 拡散光の強さを設定します 
    GL.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, this.lightAmbient, 0); // 環境光の強さを設定します
    
//    GL.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 100.0f);
    setMaterial(0.5f, 0.5f, 0.5f);
    
    // ビュー変換
    final EyeModel eye = this.configuration.getEye();
    final LookAtPointModel lookAtPoint = this.configuration.getLookAtPoint();
    GLU.gluLookAt(gl, eye.getX(), eye.getY(), eye.getZ(), lookAtPoint.getX(), lookAtPoint.getY(), lookAtPoint.getZ(), 0.0F, 0.0F, 1.0F);

    GL.glTranslatef(0.0f, this.translationY, -this.translationZ);
    GL.glRotatef(this.rotationY, 0.0f, 1.0f, 0.0f);
    GL.glRotatef(this.rotationZ, 0.0f, 0.0f, 1.0f);

    GL.glScalef(this.scale, this.scale, this.scale);
    
//    GL.glEnable(GL.GL_DEPTH_TEST); // 奥行き判定を有効にします 

    final boolean isAxisShowing = this.configuration.getBaseCoordinate().isAxisShowing();
    
    if (this.rootObjects != null) {
      for (final OpenglesGroupObject topObject : this.rootObjects) {
        topObject.setShowingAxis(isAxisShowing);
        topObject.display(gl);
      }
    }
    
    this.grid.display(gl);
    
    onSurfaceChanged(null, (int)bounds.getWidth(), (int)bounds.getHeight());
  }
  
  private void createDepthBuffer(int width, int height) {
	  if (depthBuffer != null) {
		  GL.glDeleteRenderbuffers(1, depthBuffer);
		  depthBuffer = IntBuffer.allocate(1);
	  }
	  
	  GL.glGenRenderbuffers(1, depthBuffer);
	  GL.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthBuffer.get(0));
	  GL.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT16, width, height);
	  GL.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthBuffer.get(0));
	  GL.glEnable(GL.GL_DEPTH_TEST); // 奥行き判定を有効にします 
  }
  
  private void createColorBuffer(int width, int height) {
	  if (colorBuffer != null) {
		  GL.glDeleteRenderbuffers(1, colorBuffer);
		  colorBuffer = IntBuffer.allocate(1);
	  }
	  
	  GL.glGenRenderbuffers(1, colorBuffer);
	  GL.glBindRenderbuffer(GL.GL_RENDERBUFFER, colorBuffer.get(0));
	  GL.glRenderbufferStorage(GL.GL_FRAMEBUFFER, GL.GL_RGBA, width, height);
	  GL.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0,
			  GL.GL_RENDERBUFFER, colorBuffer.get(0));
  }
  
  private void createDepthColorBuffer() {
	  GL.glGenRenderbuffers(1, colorBuffer);
	  GL.glBindRenderbuffer(GL.GL_RENDERBUFFER, colorBuffer.get(0));
	  
	  GL.glGenRenderbuffers(1, depthBuffer);
	  GL.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthBuffer.get(0));
	  
	  GL.glGenRenderbuffers(1, frameBuffer);
	  GL.glBindRenderbuffer(GL.GL_RENDERBUFFER, frameBuffer.get(0));
	  
	  GL.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_RENDERBUFFER, colorBuffer.get(0));
	  
	  GL.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthBuffer.get(0));
  }
  
	/**
	 * マテリアルを設定します。
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
  private void setMaterial(float r, float g, float b) {
	  float a = 1.0f;
	  // マテリアルの環境光色の指定
	  GL.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, new float[] { r, g, b, a }, 0);

	  // マテリアルの拡散光色の指定
	  GL.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, new float[] { r, g, b, a }, 0);

	  // マテリアルの鏡面光色と鏡面指数の指定
	  GL.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, new float[] { r, g, b, a }, 0);
	  GL.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 100.0f);
  }

  /**
   * {@inheritDoc}
   */
  public void onSurfaceChanged(GL gl, int width, int height) {
    //ビューポート変換
    GL.glViewport(0, 0, width, height);
    this.aspect = (float)width / (float)height;
  }

  /**
   * {@inheritDoc}
   */
  public void setRootGroups(List<GroupModel> rootGroups, GroupObjectManager manager) {
    this.rootObjects = createGroupObjects(rootGroups, manager);
  }

  /**
   * グループオブジェクトを生成します。
   * 
   * @param groupModels グループモデル
   * @param manager オブジェクトグループマネージャ
   * @return グループオブジェクト
   */
  private List<OpenglesGroupObject> createGroupObjects(List<GroupModel> groupModels, GroupObjectManager manager) {
    final OpenglesObjectFactory factory = new OpenglesObjectFactory(manager);
    
    final List<OpenglesGroupObject> groupObjects = new ArrayList<OpenglesGroupObject>();
    for (final GroupModel groupModel : groupModels) {
      final OpenglesGroupObject objectGroup = factory.create(groupModel);
      groupObjects.add(objectGroup);
    }
    return groupObjects;
  }
  
  /**
   * {@inheritDoc}
   */
  public void setConfiguration(ConfigurationModel configuration) {
    if (configuration == null) {
      return;
    }
    
    this.configuration = configuration;
    this.grid.setConfiguration(configuration);    
    
    updateDisplay();
  }
  
  /**
   * {@inheritDoc}
   */
  public ConfigurationModel getConfiguration() {
    return this.configuration;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isRequiredToCallDisplay() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public void updateDisplay() {
    this.glkView.display();//再描画
  }
  
  /**
   * Y軸周りの回転角度[rad]を返します。
   * 
   * @return Y軸周りの回転角度[rad]
   */
  public float getRotationY() {
    return this.rotationY;
  }

  /**
   * Z軸周りの回転角度[rad]を返します。
   * 
   * @return Z軸周りの回転角度[rad]
   */
  public float getRotationZ() {
    return this.rotationZ;
  }

  /**
   * Y軸周りの回転角度[rad]を設定します。
   * 
   * @param rotation Y軸周りの回転角度[rad]
   */
  public void rotateY(float rotation) {
    this.rotationY = this.rotationY + rotation;
  }
  
  /**
   * Z軸周りの回転角度[rad]を設定します。
   * 
   * @param rotation Z軸周りの回転角度[rad]
   */
  public void rotateZ(float rotation) {
    this.rotationZ = this.rotationZ + rotation;
  }

  /**
   * 拡大縮小倍率のを設定します。　
   * 
   * @param scale スケール
   */
  public void setScale(float scale) {
    this.scale = scale;
  }

  /**
   * スケールを返します。
   * 
   * @return　スケール
   */
  public float getScale() {
    return this.scale;
  }

  /**
   * Y軸方向の移動量を設定します。
   * 
   * @param translation Y軸方向の移動量
   */
  public void translateY(float translation) {
    this.translationY = this.translationY + translation;
  }

  /**
   * Z軸方向の移動量を設定します。
   * 
   * @param translation Z軸方向の移動量
   */
  public void translateZ(float translation) {
    this.translationZ = this.translationZ + translation;
  }
  
  /**
   * 移動・回転・拡大・縮小等の操作をリセットし、初期状態に戻します。
   */
  public void resetToInitialState() {
    this.translationY = 0;
    this.translationZ = 0;
    this.rotationY = 0;
    this.rotationZ = 0;
    this.scale = 1;
  }
}
