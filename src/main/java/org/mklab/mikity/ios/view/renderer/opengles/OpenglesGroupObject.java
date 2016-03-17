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

import java.util.ArrayList;
import java.util.List;

import org.mklab.mikity.ios.GL;
import org.mklab.mikity.model.Coordinate;
import org.mklab.mikity.model.GroupObject;
import org.mklab.mikity.model.xml.simplexml.model.GroupModel;
import org.mklab.mikity.model.xml.simplexml.model.RotationModel;
import org.mklab.mikity.model.xml.simplexml.model.TranslationModel;


/**
 * グループオブジェクトを表すクラスです。
 * 
 * @author ohashi
 * @version $Revision$, 2013/02/06
 */
public class OpenglesGroupObject implements GroupObject, OpenglesObject {
  /** グループの要素。 */
  private List<OpenglesObject> elements = new ArrayList<OpenglesObject>();
  /** 座標系の基準。 */
  private Coordinate baseCoordinate;
  /** 座標系。 */
  private Coordinate coordinate = new Coordinate();
  /** 名前。 */
  private String name;
  /** ID。 */
  private int id = 0;
  /** モデルデータ。 */
  private GroupModel group;
  /** 座用軸を描画するならばtrue。 */
  private boolean isShowingAxis = false;
  /** シリアル番号。 */
  private static int serialID = 0;

  /**
   * 新しく生成された<code>OpenglesObjectGroup</code>オブジェクトを初期化します。
   * @param id ID
   * @param group モデルデータ
   */
  private OpenglesGroupObject(int id, GroupModel group) {
    this.id = id;
    this.group = group;
  }
  
  /**
   * ファクトリーメソッドです。
   * @return グループ
   */
  public static OpenglesGroupObject create() {
    return new OpenglesGroupObject(serialID++, null);
  }

  /**
   * ファクトリーメソッドです。
   * @param group モデルデータ
   * @return グループ
   */
  public static OpenglesGroupObject create(GroupModel group) {
    return new OpenglesGroupObject(serialID++, group);
  }
  
  /**
   * {@inheritDoc}
   */
  public GroupModel getGroup() {
    return this.group;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.baseCoordinate == null) ? 0 : this.baseCoordinate.hashCode());
    result = prime * result + ((this.coordinate == null) ? 0 : this.coordinate.hashCode());
    result = prime * result + ((this.group == null) ? 0 : this.group.hashCode());
    result = prime * result + this.id;
    result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
    result = prime * result + ((this.elements == null) ? 0 : this.elements.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final OpenglesGroupObject other = (OpenglesGroupObject)obj;
    if (this.baseCoordinate == null) {
      if (other.baseCoordinate != null) return false;
    } else if (!this.baseCoordinate.equals(other.baseCoordinate)) return false;
    if (this.coordinate == null) {
      if (other.coordinate != null) return false;
    } else if (!this.coordinate.equals(other.coordinate)) return false;
    if (this.group == null) {
      if (other.group != null) return false;
    } else if (!this.group.equals(other.group)) return false;
    if (this.id != other.id) return false;
    if (this.name == null) {
      if (other.name != null) return false;
    } else if (!this.name.equals(other.name)) return false;
    if (this.elements == null) {
      if (other.elements != null) return false;
    } else if (!this.elements.equals(other.elements)) return false;
    return true;
  }

  /**
   * オブジェクトを追加します。
   * 
   * @param element オブジェクト
   */
  public void addElement(OpenglesObject element) {
    this.elements.add(element);
  }

  /**
   * 座標系の基準を設定します。
   * 
   * @param coordinate 座標系の基準
   */
  public void setBaseCoordinate(Coordinate coordinate) {
    this.baseCoordinate = coordinate;
  }

  /**
   * {@inheritDoc}
   */
  public void display(GL gl) {
    GL.glPushMatrix();

    if (this.baseCoordinate != null) {
      applyCoordinate(gl, this.baseCoordinate);
    }

    applyCoordinate(gl, this.coordinate);

    for (final OpenglesObject object : this.elements) {
      object.display(gl);
    }

    GL.glPopMatrix();
  }
  
  /**
   * GLによる座標変換を適用します。
   * 
   * @param gl GL
   * @param coordinateArg 座標
   */
  private void applyCoordinate(GL gl, Coordinate coordinateArg) {
    final TranslationModel translation = coordinateArg.getTranslation();
    final RotationModel rotation = coordinateArg.getRotation();
    GL.glTranslatef(translation.getX(), translation.getY(), translation.getZ());
    GL.glRotatef((float)Math.toDegrees(rotation.getX()), 1.0f, 0.0f, 0.0f);
    GL.glRotatef((float)Math.toDegrees(rotation.getY()), 0.0f, 1.0f, 0.0f);
    GL.glRotatef((float)Math.toDegrees(rotation.getZ()), 0.0f, 0.0f, 1.0f);
  }

  /**
   * {@inheritDoc}
   */
  public void setCoordinate(Coordinate coordinate) {
    this.coordinate = coordinate;
  }

  /**
   * {@inheritDoc}
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if (this.name == null) {
      return super.toString();
    }

    if (this.coordinate == null) {
      return this.name;
    }

    return this.name + this.coordinate;
  }

  /**
   * {@inheritDoc}
   */
  public void setShowingAxis(boolean isShowingAxis) {
    this.isShowingAxis = isShowingAxis;
    
    for (final OpenglesObject object : this.elements) {
      object.setShowingAxis(isShowingAxis);
    }
  }

}
