package org.mklab.mikity.ios;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellAccessoryType;
import org.robovm.apple.uikit.UITableViewCellSelectionStyle;
import org.robovm.apple.uikit.UITableViewCellStyle;
import org.robovm.apple.uikit.UITableViewController;

public class Menu extends UITableViewController{
	public Menu() {
		UITableView view = getTableView();
				
		UITableViewCell cell = view.dequeueReusableCell("model");
		
		if (cell == null) {
			cell = new UITableViewCell(
					UITableViewCellStyle.Default, "model");
			cell.setAccessoryType(UITableViewCellAccessoryType.DetailDisclosureButton);
			
			cell.setSelectionStyle(UITableViewCellSelectionStyle.Default);
			cell.getTextLabel().setText("Model");
			cell.setAccessoryType(UITableViewCellAccessoryType.DetailButton);
			
			cell.setEditing(true);
			
			view.addSubview(cell);
			
		}
	}

}
