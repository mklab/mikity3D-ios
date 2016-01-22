package org.mklab.mikity.ios.viewcontroller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.mklab.mikity.control.AnimationTaskListener;
import org.mklab.mikity.ios.control.AnimationTask;
import org.mklab.mikity.ios.messenger.CanvasMenuInterface;
import org.mklab.mikity.ios.view.renderer.opengles.OpenglesObjectRenderer;
import org.mklab.mikity.model.GroupObjectManager;
import org.mklab.mikity.model.xml.Mikity3dFactory;
import org.mklab.mikity.model.xml.simplexml.ConfigurationModel;
import org.mklab.mikity.model.xml.simplexml.Mikity3DModel;
import org.mklab.mikity.model.xml.simplexml.config.EyeModel;
import org.mklab.mikity.model.xml.simplexml.config.LightModel;
import org.mklab.mikity.model.xml.simplexml.config.LookAtPointModel;
import org.mklab.mikity.model.xml.simplexml.model.AnimationModel;
import org.mklab.mikity.model.xml.simplexml.model.GroupModel;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matx.MatxMatrix;
import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.NSProcessInfo;
import org.robovm.apple.glkit.GLKView;
import org.robovm.apple.glkit.GLKViewController;
import org.robovm.apple.uikit.UIGestureRecognizer;
import org.robovm.apple.uikit.UIGestureRecognizerDelegate;
import org.robovm.apple.uikit.UIGestureRecognizerState;
import org.robovm.apple.uikit.UIPanGestureRecognizer;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UITapGestureRecognizer;
import org.robovm.apple.uikit.UITouch;
import org.robovm.objc.Selector;
import org.robovm.objc.annotation.BindSelector;
import org.robovm.objc.annotation.Method;
import org.robovm.rt.bro.annotation.Callback;

/**
 * 
 */
public class Canvas extends GLKViewController implements UIGestureRecognizerDelegate {
	/** レンダー */
	private OpenglesObjectRenderer objectRenderer;
	
	/** 設定モデル */
	private ConfigurationModel configuration;
	
	float prevX = 0;
	float prevY = 0;
	
	Timer timer = new Timer();

	private double[] timeTable;
	
	/** アニメーションの開始時間 */
	private long startTime;
	/** アニメーションの終了時間。 */
	private double stopTime;
	/** アニメーションの一時停止時間 */
	private long pausedTime;
	/** アニメーションの遅延時間 */
	private long delayTime;

	/** Mikity3dモデル */
	private Mikity3DModel root;
		
	private GroupObjectManager manager;

	private Map<String, DoubleMatrix> sourceData = new HashMap<String, DoubleMatrix>();

	private boolean playable = true;
	private AnimationTask animationTask;
	
	/** ポーズボタンが押されたならばtrue */
	boolean isPaused = false;

	/** 繰り返し再生中ならばtrue */
	boolean isRepeating = false;
	
	private CanvasMenuInterface messenger;
	
	public Canvas(CanvasMenuInterface messenger) {
		this.messenger = messenger;
	}
	
	@Override
	public void viewDidLoad() {
		super.viewDidLoad();
		
	    final ConfigurationModel configuration = new ConfigurationModel();
	    configuration.setEye(new EyeModel(5.0f, 0.0f, 0.0f));
	    configuration.setLookAtPoiint(new LookAtPointModel(0.0f, 0.0f, 0.0f));
	    configuration.setLight(new LightModel(10.0f, 10.0f, 20.0f));

	    this.objectRenderer = new OpenglesObjectRenderer((GLKView) getView(), configuration);
	    
	    
	    UIPanGestureRecognizer panGesture = new UIPanGestureRecognizer();
	    Selector selector = Selector.register("handlePan:");
	    panGesture.addTarget(this, selector);
	    panGesture.setDelegate(this);
	    getView().addGestureRecognizer(panGesture);
	    
//	    UITapGestureRecognizer tapGesture = new UITapGestureRecognizer();
//	    tapGesture.addTarget(this, Selector.register("handleTap:"));
	    
//	    tapGesture.setNumberOfTapsRequired(1);
//	    getView().addGestureRecognizer(tapGesture);
	    
		CGRect bounds = UIScreen.getMainScreen().getBounds();
		this.objectRenderer.onSurfaceChanged(null, (int)bounds.getWidth(), (int)bounds.getHeight());
		this.objectRenderer.onSurfaceCreated(null);
	}
	
	@Override
	public void draw(GLKView view, CGRect rect) {
		this.objectRenderer.onDrawFrame(null);
	}
	
	/**
	 * モデルデータをストリームから読み込みます。
	 * 
	 * @param input モデルファイル
	 * @throws IOException ファイルを読み込めない場合
	 */
	public void loadModelData(File file) {
		try {
			this.root = new Mikity3dFactory().loadFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		prepareObjectGroupManager();
	    prepareRenderer();
	    
	    this.messenger.setModelData(root);

	    final List<GroupModel> rootGroups = this.root.getScene(0).getGroups();
	    if (hasAnimation(rootGroups)) {
	      this.manager.setHasAnimation(true);
	    }
	}
	
	/**
	 * ソースデータを読み込みます。
	 * 
	 * @param file ソースデータのインプットストリーム
	 */
	public void readSourceData(final String sourceId, final File file, final String sourceFilePath) {
		try {
			if (sourceFilePath.toLowerCase().endsWith(".mat") || sourceFilePath.startsWith("/document")) { //$NON-NLS-1$ //$NON-NLS-2$
				this.sourceData.put(sourceId, (DoubleMatrix)MatxMatrix.readMatFormat(file));
			} else if (sourceFilePath.toLowerCase().endsWith(".csv") || sourceFilePath.startsWith("/document")) { //$NON-NLS-1$ //$NON-NLS-2$
				this.sourceData.put(sourceId, DoubleMatrix.readCsvFormat(file).transpose());
			} else if (sourceFilePath.toLowerCase().endsWith(".txt") || sourceFilePath.startsWith("/document")) { //$NON-NLS-1$ //$NON-NLS-2$
				this.sourceData.put(sourceId, DoubleMatrix.readSsvFormat(file).transpose());
			} else {
				this.sourceData.put(sourceId, DoubleMatrix.readSsvFormat(file).transpose());
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
	    	// TODO not implemented
	    	e.printStackTrace();
	    } catch (IllegalArgumentException e) {
	    	// TODO not implemented
	    	e.printStackTrace();
	    } catch (IllegalAccessError e) {
	    	// TODO not implemented
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * MovableGroupManagerに時間データを登録します。
	 * 
	 * @param sourceId ソースID
	 */
	public void addSource(String sourceId) {
		try {
			this.manager.addSource(sourceId, this.sourceData.get(sourceId));
		} catch (IllegalAccessError e) {
			// TODO not implemented
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO not implemented
			e.printStackTrace();
		}
	}
	
	private void prepareTimeTable() {
		final int dataSize = this.manager.getDataSize();
		this.timeTable = new double[dataSize];
		this.stopTime = this.manager.getStopTime();
		for (int i = 0; i < this.timeTable.length; i++) {
			this.timeTable[i] = this.stopTime * ((double)i / this.timeTable.length);
		}
	}
	
	/**
	 * ObjectGroupManagerを返します。
	 * 
	 * @return ObjectGroupManager
	 */
	public GroupObjectManager getObjectGroupManager() {
		return this.manager;
	}

	/**
	 * オブジェクトレンダラーを返します。
	 * 
	 * @return オブジェクトレンダラー
	 */
	public OpenglesObjectRenderer getObjectRender() {
		return this.objectRenderer;
	}
	
	/**
	 * アニメーションを繰り返し再生します。 
	 */
	public void repeatAnimation() {
		this.isRepeating = true;
		runAnimation();
	}
	
	/**
	 * アニメーションを開始します。
	 */
	public void runAnimation() {
		if (this.manager == null) {
			// TODO
//			showAlertMessageInDialog(getString(R.string.modelDataIsNotReady));
			return;
		}
		if (this.manager.isSourceReady() == false) {
			// TODO
//			showAlertMessageInDialog(getString(R.string.sourceDataIsNotReady));
			return;
		}

	    if (this.playable == false) {
	    	this.timer.cancel();
	    }
	    
	    if (this.isPaused == false) {
	    	this.startTime = (long) (NSProcessInfo.getSharedProcessInfo().getSystemUptime()*1000);
	    	this.delayTime = 0;
	    }

	    this.manager.prepareMovingGroupObjects();

	    prepareTimeTable();

	    this.stopTime = this.manager.getStopTime();

	    if (this.isPaused) {
	    	final double upTime = NSProcessInfo.getSharedProcessInfo().getSystemUptime()*1000;
	    	this.delayTime += (long)upTime - this.pausedTime;
	    }
	    this.isPaused = false;
	    
	    this.animationTask = new AnimationTask(this.startTime, this.stopTime, getObjectGroupManager(), getObjectRender(), this.delayTime);
	   // TODO not implemented 
//	    this.animationTask.setSpeedScale(this.canvasActivity.ndFragment.animationSpeedRate/1000.0);
	    this.animationTask.addAnimationTaskListener(new AnimationTaskListener() {
	    	/**
	    	 * {@inheritDoc}
	    	 */
	    	public void tearDownAnimation() {
	    		Canvas.this.playable = true;
	    		if (Canvas.this.isRepeating) {
	    			runAnimation();
	    		}
	    	}

	    	/**
	    	 * {@inheritDoc}
	    	 */
	    	public void setUpAnimation() {
	    		// nothing to do
	    	}
	    });

	    this.playable = false;
	    this.timer = new Timer();
	    this.timer.schedule(this.animationTask, 0, 30);
	}
	
	/**
	 * アニメーションを停止します。 
	 */
	public void stopAnimation() {
		this.timer.cancel();
		this.playable = true;
		this.isPaused = false;
		this.isRepeating = false;
	}
	
	/**
	 * アニメーションを一時停止します。 
	 */
	public void pauseAnimation() {
		if (this.isPaused) {
			return;
		}
	    
		if (this.animationTask != null) {
			this.timer.cancel();
			this.isPaused = true;
			this.playable = true;
	    	final double upTime = NSProcessInfo.getSharedProcessInfo().getSystemUptime()*1000;
			setPuasedTime((long)upTime);
		}
	}
	
	/**
	 * モデルを返します。
	 * 
	 * @return モデル
	 */
	public Mikity3DModel getRoot() {
		return this.root;
	}
	
	/**
	 * レンダーを準備します。
	 */
	public void prepareRenderer() {
		final List<GroupModel> rootGroups = this.root.getScene(0).getGroups();
		final ConfigurationModel configuration = this.root.getConfiguration(0);

		this.manager.clearGroupObjects();
		this.objectRenderer.setRootGroups(rootGroups, this.manager);
		this.objectRenderer.setConfiguration(configuration);
	}
	
	/**
	 * モデルをリセットし、初期状態に戻します。 
	 */
	public void resetToInitialState() {
		this.objectRenderer.resetToInitialState();
	    this.objectRenderer.updateDisplay();
	}
	
	/**
	 * グリッドを表示するか設定します。
	 * 
	 * @param isGridShowing 表示するならばtrue
	 */
	public void setGridShowing(boolean isGridShowing) {
		if (this.root == null) {
			return;
		}
		this.root.getConfiguration(0).getBaseCoordinate().setGridShowing(isGridShowing);
	}
	
	/**
	 * グリッドを表示するか判定します。
	 * 
	 * @return 表示するならばtrue
	 */
	public boolean isGridShowing() {
		if (this.root == null) {
			return false;
			}
		return this.root.getConfiguration(0).getBaseCoordinate().isGridShowing();	
	}
	
	/**
	 * 座標軸を表示するか設定します。
	 * 
	 * @param isAxisShowing 表示するならばtrue
	 */
	public void setAxisShowing(boolean isAxisShowing) {
		if (this.root == null) {
			return;
		}
		this.root.getConfiguration(0).getBaseCoordinate().setAxisShowing(isAxisShowing);
	}
	
	/**
	 * 座標軸を表示するか判定します。
	 * 
	 * @return 表示するならばtrue
	 */
	public boolean isAxisShowing() {
		if (this.root == null) {
			return false;
		}
		return this.root.getConfiguration(0).getBaseCoordinate().isAxisShowing();
	}
	
	/**
	 * ObjectGroupManagerを準備します。
	 */
	protected void prepareObjectGroupManager() {
		this.manager = new GroupObjectManager();
	}
	
	private boolean hasAnimation(List<GroupModel> groups) {
		for (final GroupModel group : groups) {
			final AnimationModel[] animations = group.getAnimations();
			for (final AnimationModel animation : animations) {
				if (animation.exists()) {
					return true;
				}
			}
			if (hasAnimation(group.getGroups())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 一時停止が押された時間を設定します。
	 * 
	 * @param pausedTime 一時停止が押された時間
	 */
	public void setPuasedTime(long pausedTime) {
		this.pausedTime = pausedTime;
	}
	
	@Method
	public void handlePan(UIPanGestureRecognizer gestureRecognizer) {
		if (gestureRecognizer.getState() == UIGestureRecognizerState.Began 
				|| gestureRecognizer.getState() == UIGestureRecognizerState.Changed) {
			CGPoint translation = gestureRecognizer.getTranslation(getView());
				
			float rotationY = (float) (translation.getY() / 5);
			float rotationZ = (float) (translation.getX() / 5);
			this.objectRenderer.rotateY(rotationY);
			this.objectRenderer.rotateZ(rotationZ);
		}
	}
	
	@Callback @BindSelector("handleTap:")
	private static void handleTap(Canvas self, Selector cmd, UITapGestureRecognizer gestureRecognizer) {
		String test = gestureRecognizer.toString();
	}

	@Override
	public boolean shouldBegin(UIGestureRecognizer gestureRecognizer) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldRecognizeSimultaneously(UIGestureRecognizer gestureRecognizer,
			UIGestureRecognizer otherGestureRecognizer) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldRequireFailure(UIGestureRecognizer gestureRecognizer,
			UIGestureRecognizer otherGestureRecognizer) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldBeRequiredToFail(UIGestureRecognizer gestureRecognizer,
			UIGestureRecognizer otherGestureRecognizer) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldReceiveTouch(UIGestureRecognizer gestureRecognizer, UITouch touch) {
		// TODO Auto-generated method stub
		return true;
	}
}
