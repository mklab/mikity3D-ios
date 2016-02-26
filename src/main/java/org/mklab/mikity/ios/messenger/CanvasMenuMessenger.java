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
 * CanvasMenu間のインターフェースを表すクラス
 */
public class CanvasMenuMessenger {
	private Canvas canvas;
	private Menu menu;
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	/**
	 * キャンバスを割り当てる
	 * 
	 * @param canvas
	 */
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public Menu getMenu() {
		return menu;
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
		String path = readFilePath(directory, selectFile);
		File modelFile = new File(path);
		
		this.canvas.loadModelData(modelFile);
	}
	
	public void readSource(DirectoryPath directory, String selectFile) {
		String path = readFilePath(directory, selectFile);
		File sourceFile = new File(path);
		
		this.canvas.readSourceData("0", sourceFile, path);
		this.canvas.addSource("0");
	}
	
	private String readFilePath(DirectoryPath directory, String selectFile) {
		String[] selectFileName = selectFile.split(Pattern.quote("."));
		
		String fullPath = directory.getPathName() + "/" + selectFileName[0];
		
		String path = NSBundle.getMainBundle().findResourcePath(fullPath, selectFileName[1]);
		
		return path;
	}
	
	public void setModelData(Mikity3DModel model) {
		boolean isAxisShowing = model.getConfiguration(0).getBaseCoordinate().isAxisShowing();
		boolean isGridShowing = model.getConfiguration(0).getBaseCoordinate().isGridShowing();
		
		this.menu.setGridShowing(isGridShowing);
		this.menu.setAxisShowing(isAxisShowing);
	}
	
	public void setGridShowing(boolean isGridShowing) {
		this.canvas.setGridShowing(isGridShowing);
	}
	
	public void setAxisShowing(boolean isAxisShowing) {
		this.canvas.setAxisShowing(isAxisShowing);
	}
}
