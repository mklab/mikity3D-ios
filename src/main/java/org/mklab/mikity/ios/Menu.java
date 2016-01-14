package org.mklab.mikity.ios;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.uikit.UIBarButtonSystemItem;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIButtonType;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControlState;
import org.robovm.apple.uikit.UIFont;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UIScrollView;
import org.robovm.apple.uikit.UISwitch;
import org.robovm.apple.uikit.UIViewController;

/**
 * メニュー画面を表すクラス
 */
public class Menu extends UIViewController{
	public Menu() {
		
		UIScrollView scrollView = new UIScrollView();
		scrollView.setFrame(new CGRect(0, 0, 300, 1000));
		scrollView.setContentSize(new CGSize(300, 100));
		scrollView.setBackgroundColor(UIColor.white());
		setView(scrollView);
		
		// play speed
		UILabel playSpeed = createLabel("Play Speed	" + 1.00, new CGRect(10, 10, 300, 30));
		UIButton backPlayButton = createButton("<<", new CGRect(100, 50, 50, 30));
		UIButton fastPlayButton = createButton(">>", new CGRect(160, 50, 50, 30));
		scrollView.addSubview(playSpeed);
		scrollView.addSubview(backPlayButton);
		scrollView.addSubview(fastPlayButton);
		
		// model button
		UIButton modelButton = createButton("Model", new CGRect(10, 100, 70, 30));
		scrollView.addSubview(modelButton);
		
		// model edit button
		UIButton modelEditButton = createButton("Model Edit", new CGRect(10, 150, 120, 30));
		scrollView.addSubview(modelEditButton);
		
		// sensor switches
		UILabel slope = createLabel("slope sensor", new CGRect(10, 190, 150, 30));
		UISwitch slopeSwitch = new UISwitch(new CGRect(170, 190, 50, 30));
		scrollView.addSubview(slope);
		scrollView.addSubview(slopeSwitch);
		
		UILabel accel = createLabel("accel sensor", new CGRect(10, 230, 150, 30));
		UISwitch accelSwitch = new UISwitch(new CGRect(170, 230, 50, 30));
		scrollView.addSubview(accel);
		scrollView.addSubview(accelSwitch);
		
		UILabel rockRotation = createLabel("rotation rock", new CGRect(10, 270, 150, 30));
		UISwitch rockRotationSwitch = new UISwitch(new CGRect(170, 270, 50, 30));
		scrollView.addSubview(rockRotation);
		scrollView.addSubview(rockRotationSwitch);
		
		UILabel displayGrid = createLabel("display grid", new CGRect(10, 310, 150, 30));
		UISwitch displayGridSwitch = new UISwitch(new CGRect(170, 310, 50, 30));
		scrollView.addSubview(displayGrid);
		scrollView.addSubview(displayGridSwitch);
		
		UILabel displayAxis = createLabel("display axis", new CGRect(10, 350, 150, 30));
		UISwitch displayAxisSwitch = new UISwitch(new CGRect(170, 350, 50, 30));
		scrollView.addSubview(displayAxis);
		scrollView.addSubview(displayAxisSwitch);
		
		// sample
		UILabel sampleLabel = createLabel("Samples", new CGRect(10, 410, 100, 30));
		UIButton sampleModelButton = createButton("Model", new CGRect(10, 450, 70, 30));
		UIButton sampleSourceButton = createButton("Source", new CGRect(10, 490, 80, 30));
		scrollView.addSubview(sampleLabel);
		scrollView.addSubview(sampleModelButton);
		scrollView.addSubview(sampleSourceButton);
	}
	
	/**
	 * ラベルを作成する
	 * 
	 * @param text ラベルに表示する文字
	 * @param frame ラベルの位置及びサイズ
	 * @return 作成されたラベル
	 */
	private UILabel createLabel(String text, CGRect frame) {
		UILabel label = new UILabel(frame);
		
		label.setFont(UIFont.getBoldSystemFont(22));
		label.setTextColor(UIColor.black());
		label.setText(text);
		
		return label;
	}
		
	/**
	 * ボタンを作成する
	 * 
	 * @param title ボタン名
	 * @param frame ボタンの位置及びサイズ
	 * @return 作成されたボタン
	 */
	private UIButton createButton(String title, CGRect frame) {
		UIButton button = new UIButton(UIButtonType.RoundedRect);
		button.setFrame(frame);
		button.setTitle(title, UIControlState.Normal);
		button.setBackgroundColor(UIColor.black());
		button.setTitleColor(UIColor.white(), UIControlState.Normal);
		if (button.getState() == UIControlState.Disabled) {
			button.setBackgroundColor(UIColor.gray());
		}
		button.getTitleLabel().setFont(UIFont.getBoldSystemFont(22));

		return button;
	}
}
