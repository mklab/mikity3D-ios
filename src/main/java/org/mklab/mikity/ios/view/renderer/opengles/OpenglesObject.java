
package org.mklab.mikity.ios.view.renderer.opengles;

import org.mklab.mikity.ios.GL;

/**
 * OpenGL ESのオブジェクトを表すインターフェースです。
 * 
 * @author ohashi
 * @version $Revision$, 2013/02/06
 */
public interface OpenglesObject {
  /**
   * オブジェクトを表示します。
   * @param gl GL
   */
  void display(GL gl);
  
  /**
   * 座標軸を描画するか設定します。
   * 
   * @param isShowingAxis 座標軸を描画するならばtrue
   */
  void setShowingAxis(boolean isShowingAxis);
}
