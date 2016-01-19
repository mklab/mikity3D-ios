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

	public DirectoryPath(String rootPathName) {
		this.dirName = rootPathName;
		this.pathName = rootPathName;
		this.root = rootPathName;
	}
	
	public void accessDirectory(String dirName) {
		this.dirName = dirName;
		this.pathName += ("/" + dirName);
	}
	
	public void backDirectory() {
		if (!isRoot()) {
			int last_path = this.pathName.lastIndexOf("/");
			this.pathName = this.pathName.substring(0, last_path);
		
			int last_dir = this.pathName.lastIndexOf("/");
			
			if (last_dir == -1) this.dirName = this.pathName;
			else this.dirName = this.pathName.substring(last_dir+1);
		}
	}
	
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
