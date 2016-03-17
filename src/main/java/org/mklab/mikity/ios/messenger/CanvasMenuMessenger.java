/**
 * Copyright (C) 2015 MKLab.org (Koga Laboratory)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mklab.mikity.ios.messenger;

import java.io.File;
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
	
	/**
	 * モデルファイルを読み込む
	 * ※MenuからCanvasへのメッセージ送信
	 * 
	 * @param directory 読み込み先のファイルパス
	 * @param selectFile 読み込むファイル
	 */
	public void readModel(DirectoryPath directory, String selectFile) {
		String path = readFilePath(directory, selectFile);
		File modelFile = new File(path);
		
		this.canvas.loadModelData(modelFile);
	}
	
	/**
	 * ソースファイルを読み込む
	 * ※MenuからCanvasへのメッセージ送信
	 * 
	 * @param path 読み込み先のファイルパス
	 * @param selectFile
	 */
	public void readSource(DirectoryPath path, String selectFile) {
		String paths = readFilePath(path, selectFile);
		File sourceFile = new File(paths);
		
		this.canvas.readSourceData("0", sourceFile, paths);
		this.canvas.addSource("0");
	}
	
	private String readFilePath(DirectoryPath directory, String selectFile) {
		String[] selectFileName = selectFile.split(Pattern.quote("."));
		
		String fullPath = directory.getPathName() + "/" + selectFileName[0];
		
		String path = NSBundle.getMainBundle().findResourcePath(fullPath, selectFileName[1]);
		
		return path;
	}
	
	/**
	 * モデルデータの情報を反映する
	 * ※CanvasからMenuへのメッセージ送信
	 * 
	 * @param model モデルデータクラス
	 */
	public void setModelData(Mikity3DModel model) {
		boolean isAxisShowing = model.getConfiguration(0).getBaseCoordinate().isAxisShowing();
		boolean isGridShowing = model.getConfiguration(0).getBaseCoordinate().isGridShowing();
		
		this.menu.setGridShowing(isGridShowing);
		this.menu.setAxisShowing(isAxisShowing);
	}
	
	/**
	 * グリッド表示の情報を反映する
	 * ※MenuからCanvasへのメッセージ送信
	 * 
	 * @param isGridShowing グリッド表示フラグ
	 */
	public void setGridShowing(boolean isGridShowing) {
		this.canvas.setGridShowing(isGridShowing);
	}
	
	/**
	 * 座標軸表示の情報を反映する
	 * ※MenuからCanvasへのメッセージ送信
	 * 
	 * @param isAxisShowing 座標軸表示フラグ
	 */
	public void setAxisShowing(boolean isAxisShowing) {
		this.canvas.setAxisShowing(isAxisShowing);
	}
}
