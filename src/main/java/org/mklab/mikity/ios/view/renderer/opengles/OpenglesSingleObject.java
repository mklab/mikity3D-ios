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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.mklab.mikity.ios.GL;

import org.mklab.mikity.model.graphic.AbstractGraphicObject;
import org.mklab.mikity.model.graphic.GraphicObject;
import org.mklab.mikity.model.xml.simplexml.model.ColorModel;

/**
 * OpenGL ESの単体オブジェクトを表す抽象クラスです。
 * 
 * @author koga
 * @version $Revision$, 2015/08/02
 */
public class OpenglesSingleObject implements OpenglesObject {
  /** グラフィックオブジェクト。 */
  private GraphicObject object;
  /** 座標軸。 */
  private GraphicObject[] axises;
  /** 座標軸を描画するならばtrue。 */
  private boolean isShowingAxis = false;
  
  /**
   * 新しく生成された<code>OpenglesSingleObject</code>オブジェクトを初期化します。
   * @param object グラフィックオブジェクト
   * @param axises 座標軸オブジェクト
   */
  public OpenglesSingleObject(GraphicObject object, GraphicObject[] axises) {
    this.object = object;
    this.axises = axises;
  }
  
  /**
   * {@inheritDoc}
   */
  public void display(GL gl) {
    applyTransparency(gl);
    applyColor(gl, ((AbstractGraphicObject)this.object).getColor());
    drawObject(gl);
    
    if (this.isShowingAxis && ((AbstractGraphicObject)this.object).isTransparent() == false) {
      drawAxies(gl);
    }
  }

  /**
   * オブジェクトを描画します。
   * 
   * @param gl GL
   */
  private void drawObject(GL gl) {
    final float[] vertexArray = this.object.getVertexArray();
    final float[] normalVectorArray = this.object.getNormalVectorArray();

    drawTrianglePolygons(gl, vertexArray, normalVectorArray);
  }

  /**
   * 色を適用します。
   * 
   * @param gl GL　
   */
  private void applyColor(GL gl, ColorModel color) {
    //final ColorModel color = ((AbstractGraphicObject)this.object).getColor();
    gl.glColor4f(color.getRf(), color.getGf(), color.getBf(), color.getAlphaf());
  }
  
  /**
   * 透明性を適用します。
   * 
   * @param gl GL
   */
  private void applyTransparency(GL gl) {
//    if (((AbstractGraphicPrimitive)this.object).isTransparent()) {
      gl.glEnable(GL.GL_BLEND); // ブレンドを有効にします
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
//    } else {
//      gl.glEnable(GL10.GL_BLEND); // ブレンドを有効にします
//      gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ZERO);
//    }
  }
  
  /**
   * オブジェクトの座標軸を描画します。
   * 
   * @param gl GL
   */
  public void drawAxies(GL gl) {
    applyColor(gl, new ColorModel("red")); //$NON-NLS-1$
    gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
    drawAxis(gl, this.axises[0]);
    gl.glRotatef(-90, 0.0f, 1.0f, 0.0f);

    applyColor(gl, new ColorModel("green")); //$NON-NLS-1$
    gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
    drawAxis(gl, this.axises[1]);
    gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
    
    applyColor(gl, new ColorModel("blue")); //$NON-NLS-1$
    drawAxis(gl, this.axises[2]);
  }

  /**
   * 座標軸を描画します。
   * 
   * @param gl GL
   * @param axis 座標軸
   */
  private void drawAxis(GL gl, GraphicObject axis) {
    final float[] vertexArray = axis.getVertexArray();
    final float[] normalVectorArray = axis.getNormalVectorArray();
    
    drawTrianglePolygons(gl, vertexArray, normalVectorArray);   
  }

  
  /**
   * 三角形ポリゴンを描画します。
   * 
   * @param gl GL
   */
  private void drawTrianglePolygons(GL gl, final float[] vertexArray, final float[] normalVectorArray) {
    
    final FloatBuffer vertexBuffer = makeFloatBuffer(vertexArray);
    final FloatBuffer normalBuffer = makeFloatBuffer(normalVectorArray);
  
    // 法線配列の有効化
    gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
    // 法線バッファの指定
    gl.glNormalPointer(GL.GL_FLOAT, 0, normalBuffer);
    
    // 頂点配列の有効化
    gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
    // 頂点バッファの指定
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer);
  
    final int number = vertexArray.length/3;
    gl.glDrawArrays(GL.GL_TRIANGLES, 0, number);
  }

  /**
   * float配列をFloatBufferに変換します。
   * 
   * @param array 変換元
   * @return 変換結果
   */
  private FloatBuffer makeFloatBuffer(float[] array) {
    final FloatBuffer buffer = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    buffer.put(array).position(0);
    return buffer;
  }

  /**
   * byte配列をByteBufferに変換します。
   * 
   * @param array 変換元
   * @return 変換結果
   */
  @SuppressWarnings("unused")
  private ByteBuffer makeByteBuffer(byte[] array) {
    final ByteBuffer buffer = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
    buffer.put(array).position(0);
    return buffer;
  }
  
  /**
   * {@inheritDoc}
   */
  public void setShowingAxis(boolean isShowingAxis) {
    this.isShowingAxis = isShowingAxis;
  }
}
