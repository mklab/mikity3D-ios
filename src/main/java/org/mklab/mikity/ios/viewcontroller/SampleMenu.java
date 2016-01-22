/**
 * 
 */
package org.mklab.mikity.ios.viewcontroller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mklab.mikity.ios.DirectoryPath;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSFileManager;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.foundation.NSSearchPathDirectory;
import org.robovm.apple.foundation.NSSearchPathDomainMask;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellAccessoryType;
import org.robovm.apple.uikit.UITableViewCellSelectionStyle;
import org.robovm.apple.uikit.UITableViewCellStyle;
import org.robovm.apple.uikit.UITableViewController;

/**
 * サンプルメニューを表すクラス
 */
public class SampleMenu extends UITableViewController {
	public final static int MODEL = 0;
	public final static int SOURCE = 1;
	
	private List<String> contents = new ArrayList<>();
	
	/** 現在のディレクトリ */
	private DirectoryPath currentDirectory;
	
	/** ファイルタイプ */
	private int fileType;
	
	/** 表示元のViewController */
	private Menu menu;
	
	public SampleMenu(Menu menu, final int fileType, DirectoryPath path) {
		this.menu = menu;
		this.fileType = fileType;
		this.currentDirectory = path;
	}
	
	@Override
	public void viewDidLoad() {
		NSArray<NSURL> nsa = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.ApplicationDirectory, NSSearchPathDomainMask.UserDomainMask);
		NSURL nsu = (NSURL) nsa.first();
		String snsu = nsu.getAbsoluteString() + "sample";
		
		File ss = new File(System.getenv("HOME"),"/sample");
		String test = ss.getAbsolutePath();
		boolean as = ss.isDirectory();
		String[] test2 = ss.list();
		
		String resourcePath = NSBundle.getMainBundle().findResourcePath("sample/InvertedPendulum/Pendulum/Pendulum", "m3d");
		String resourcePath2 = NSBundle.getMainBundle().findResourcePath("sample", "");
		NSURL reURL = NSBundle.getMainBundle().findResourceURL("sample", "");
		String testr = reURL.getHost();
		
		updateDataSouce();
		
//		List<String> res = NSBundle.getMainBundle().findResourcesPaths("", this.currentDirectory.getDirectoryName());
//		for(String dir : res) {
//			int res_last = dir.lastIndexOf("/");
//			String lastString = dir.substring(res_last+1);
//			this.contents.add(lastString);
//		}
//		int res_last = res.get(0).lastIndexOf("/");
//		String lastString = res.get(0).substring(res_last+1);
//		List<String> res2 = NSBundle.getMainBundle().findResourcesPaths("", "sample"+res.get(0).substring(res_last));
//		
		
//			for(NSURL nss : NSFileManager.getDefaultManager().getContentsOfDirectoryAtPath(snsu)) {
//			}
		
//		File newFile = new File(snsu);
	}

	
	/* (non-Javadoc)
	 * @see org.robovm.apple.uikit.UITableViewController#getNumberOfRowsInSection(org.robovm.apple.uikit.UITableView, long)
	 */
	@Override
	public long getNumberOfRowsInSection(UITableView tableView, long section) {
		return this.contents.size();
	}

	/* (non-Javadoc)
	 * @see org.robovm.apple.uikit.UITableViewController#getCellForRow(org.robovm.apple.uikit.UITableView, org.robovm.apple.foundation.NSIndexPath)
	 */
	@Override
	public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		UITableViewCell cell = tableView.dequeueReusableCell("SampleMenu");
		
		if (cell == null) {
			cell = new UITableViewCell(UITableViewCellStyle.Default, "SampleMenu");
			
			cell.setSelectionStyle(UITableViewCellSelectionStyle.Default);			
		}
		
		cell.getTextLabel().setText(this.contents.get(indexPath.getItem()));
		
		return cell;
	}
	
	/* (non-Javadoc)
	 * @see org.robovm.apple.uikit.UITableViewController#didSelectRow(org.robovm.apple.uikit.UITableView, org.robovm.apple.foundation.NSIndexPath)
	 */
	@Override
	public void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
		if (this.contents.get(indexPath.getItem()).equals("..")) {
			this.currentDirectory.backDirectory();
			updateDataSouce();
		} else if (this.contents.get(indexPath.getItem()).contains(".")) {
			String selectFile = this.contents.get(indexPath.getItem());
			if (this.fileType == MODEL) {
				this.menu.setModel(selectFile);
			} else if (this.fileType == SOURCE) {
				this.menu.setSource(selectFile);
			}
			dismissViewController(true, null);
		} else {
			this.currentDirectory.accessDirectory(this.contents.get(indexPath.getItem()));
			updateDataSouce();
		}
		
		tableView.reloadData();
	}
	
	private void updateDataSouce() {
		this.contents.clear();
		List<String> res = NSBundle.getMainBundle().findResourcesPaths("", this.currentDirectory.getPathName());
		this.contents.add("..");

		for(String dir : res) {
			int res_last = dir.lastIndexOf("/");
			String lastString = dir.substring(res_last+1);
			
			if (this.fileType == MODEL) {
				if (!lastString.endsWith(".mat") && !lastString.endsWith(".csv")
												 && !lastString.endsWith(".txt")) {
					this.contents.add(lastString);
				}
			} else if (this.fileType == SOURCE) {
				if (!lastString.endsWith(".m3d") && !lastString.endsWith(".stl")) {
					this.contents.add(lastString);
				}
			}
		}
	}
}
