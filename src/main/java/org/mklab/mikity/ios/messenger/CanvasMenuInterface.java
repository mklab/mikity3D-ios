package org.mklab.mikity.ios.messenger;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.mklab.mikity.ios.DirectoryPath;
import org.mklab.mikity.ios.viewcontroller.Canvas;
import org.mklab.mikity.ios.viewcontroller.Menu;
import org.mklab.mikity.model.xml.simplexml.Mikity3DModel;
import org.robovm.apple.foundation.NSBundle;

/**
 * ファイルの読み込み、設定の割り当てを行うインターフェース
 */
public class CanvasMenuInterface {
	private Canvas canvas;
	private Menu menu;
	
	/**
	 * キャンバスを割り当てる
	 * 
	 * @param canvas
	 */
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	/**
	 * メニューを割り当てる
	 * 
	 * @param menu
	 */
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public void readModel(DirectoryPath directory, String selectFile) {
		String[] selectFileName = selectFile.split(Pattern.quote("."));
		
		String fullPath = directory.getPathName() + "/" + selectFileName[0];
		
		String path = NSBundle.getMainBundle().findResourcePath(fullPath, selectFileName[1]);
		File modelFile = new File(path);
		
		this.canvas.loadModelData(modelFile);
	}
	
	public void setModelData(Mikity3DModel model) {
		boolean isAxisShowing = model.getConfiguration(0).getBaseCoordinate().isAxisShowing();
		boolean isGridShowing = model.getConfiguration(0).getBaseCoordinate().isGridShowing();
		
		this.menu.setGridShowing(isGridShowing);
		this.menu.setAxisShowing(isAxisShowing);
	}
	
}
