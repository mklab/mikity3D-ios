/*
 * Created on 2013/01/19
 * Copyright (C) 2013 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.mikity.ios.view.renderer.opengles;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * @author koga
 * @version $Revision$, 2013/02/16
 */
public class Messages {

  private static final String BUNDLE_NAME = "org.mklab.mikity.android.view.renderrer.opengles.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  private Messages() {}

  /**
   * キーに対応するメッセージを返します。
   * @param key キー
   * @return　キーに対応するメッセージ
   */
  public static String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }
}
