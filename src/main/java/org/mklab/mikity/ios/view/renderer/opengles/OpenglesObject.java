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
