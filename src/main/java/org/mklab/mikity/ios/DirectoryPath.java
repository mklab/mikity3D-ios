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
/**
 * 
 */
package org.mklab.mikity.ios;

/**
 * ファイル名及びそのパスを表すクラス
 */
public class DirectoryPath {
	/** ディレクトリ名 */
	private String dirName;
	
	/** ルートディレクトリからのパス */
	private String pathName;
	
	/** ルートディレクトリ */
	private String root;

	/**
	 * @param rootPathName ルートディレクトリ
	 */
	public DirectoryPath(String rootPathName) {
		this.dirName = rootPathName;
		this.pathName = rootPathName;
		this.root = rootPathName;
	}
	
	/**
	 * ディレクトリにアクセスする
	 * 
	 * @param dirName アクセスするディレクトリ名
	 */
	public void accessDirectory(String dirName) {
		this.dirName = dirName;
		this.pathName += ("/" + dirName);
	}
	
	/**
	 * カレントディレクトリの１つ上位のディレクトリに戻る
	 */
	public void backDirectory() {
		if (!isRoot()) {
			int last_path = this.pathName.lastIndexOf("/");
			this.pathName = this.pathName.substring(0, last_path);
		
			int last_dir = this.pathName.lastIndexOf("/");
			
			if (last_dir == -1) this.dirName = this.pathName;
			else this.dirName = this.pathName.substring(last_dir+1);
		}
	}
	
	/**
	 * ルートディレクトリであるかを判定する
	 * 
	 * @return カレントディレクトリがルートディレクトリである場合true
	 */
	public boolean isRoot() {
		return this.pathName.equals(this.root);
	}
	
	/**
	 * @return the dirName
	 */
	public String getDirectoryName() {
		return this.dirName;
	}

	/**
	 * @return the pathName
	 */
	public String getPathName() {
		return this.pathName;
	}
}
