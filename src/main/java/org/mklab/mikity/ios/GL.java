package org.mklab.mikity.ios;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.robovm.rt.bro.*;
import org.robovm.rt.bro.annotation.*;

@Library("OpenGLES")
public class GL {
	static {
		Bro.bind(GL.class);
	}

	public static final int GL_DEPTH_BUFFER_BIT = 0x00000100;
	public static final int GL_STENCIL_BUFFER_BIT = 0x00000400;
	public static final int GL_COLOR_BUFFER_BIT = 0x00004000;
	public static final int GL_VERTEX_ARRAY = 0x00008074;
	public static final int GL_FLOAT = 0x00001406;
	public static final int GL_LINES = 0x00000001;
	public static final int GL_BLEND = 0x00000be2;
	public static final int GL_SRC_ALPHA = 0x00000302;
	public static final int GL_ONE_MINUS_SRC_ALPHA = 0x00000303;
	public static final int GL_NORMAL_ARRAY = 0x00008085;
	public static final int GL_TRIANGLES = 0x0000004;
	public static final int GL_LIGHTING = 0x00000b50;
	public static final int GL_COLOR_MATERIAL = 0x00000b57;
	public static final int GL_NORMALIZE = 0x00000ba1;
	public static final int GL_LIGHT0 = 0x00004000;
	public static final int GL_FRONT = 0x00000404;
	public static final int GL_BACK = 0x00000405;
	public static final int GL_SHININESS = 0x00001601;
	public static final int GL_POSITION = 0x00001203;
	public static final int GL_SPECULAR = 0x00001202;
	public static final int GL_DIFFUSE = 0x00001201;
	public static final int GL_AMBIENT = 0x00001200;
	public static final int GL_PROJECTION = 0x00001701;
	public static final int GL_DEPTH_TEST = 0x00000b71;
	public static final int GL_MODELVIEW = 0x00001700;
	public static final int GL_FALSE = 0x00000000;
	public static final int GL_TRUE = 0x00000001;
	public static final int GL_TRIANGLE_STRIP = 0x00000005;
	public static final int GL_UNSIGNED_BYTE = 0x00001401;
	public static final int GL_FRONT_AND_BACK = 0x00000408;
	public static final int GL_CULL_FACE = 0x00000b44;
	public static final int GL_CCW = 0x00000901;
	public static final int GL_RENDERBUFFER = 0x00008d41;
	public static final int GL_DEPTH_COMPONENT16 = 0x000081a5;
	public static final int GL_FRAMEBUFFER = 0x00008d40;
	public static final int GL_DEPTH_ATTACHMENT = 0x00008d00;
	public static final int GL_RGBA = 0x00001908;
	public static final int GL_COLOR_ATTACHMENT0 = 0x00008ce0;

	@Bridge
	public static native void glClearColor(float red, float green, float blue, float alpha);

	@Bridge
	public static native void glClear(int mask);

	@Bridge
	public static native void glColor4f(float rf, float gf, float bf, float alphaf);

	@Bridge
	public static native void glEnableClientState(int glVertexArray);

	@Bridge
	public static native void glVertexPointer(int i, int glFloat, int j, FloatBuffer vertexBuffer);

	@Bridge
	public static native void glDrawArrays(int glLines, int i, int number);

	@Bridge
	public static native void glPushMatrix();

	@Bridge
	public static native void glPopMatrix();

	@Bridge
	public static native void glTranslatef(float x, float y, float z);

	@Bridge
	public static native void glRotatef(float degrees, float f, float g, float h);

	@Bridge
	public static native void glEnable(int glBlend);

	@Bridge
	public static native void glBlendFunc(int glSrcAlpha, int glOneMinusSrcAlpha);

	@Bridge
	public static native void glNormalPointer(int glFloat, int i, FloatBuffer normalBuffer);

	@Bridge
	public static native void glLightfv(int glLight0, int glPosition, float[] lightLocation, int i);

	@Bridge
	public static native void glMaterialf(int glFront, int glShininess, float f);

	@Bridge
	public static native void glMatrixMode(int glProjection);

	@Bridge
	public static native void glLoadIdentity();

	@Bridge
	public static native void glScalef(float scale, float scale2, float scale3);

	@Bridge
	public static native void glViewport(int i, int j, int width, int height);

	@Bridge
	public static native void glMultMatrixf(float[] scratch, int i);

	@Bridge
	public static native void glOrthof(float left, float right, float bottom, float top, float f, float g);

	@Bridge
	public static native void glFrustumf(float left, float right, float bottom, float top, float zNear, float zFar);

	@Bridge
	public static native void glDrawElements(int glTriangleStrip, int i, int glUnsignedByte, ByteBuffer indexBuffer);

	@Bridge
	public static native void glMaterialfv(int glFrontAndBack, int glAmbient, float[] fs, int i);
	
	@Bridge
	public static native void glCullFace(int mode);
	
	@Bridge
	public static native void glFrontFace(int mode);
	
	@Bridge
	public static native void glDisable(int mode);
	
	@Bridge
	public static native void glDeleteRenderbuffers(int n, IntBuffer renderbuffers);
	
	@Bridge
	public static native void glGenRenderbuffers(int n, IntBuffer renderbuffers);
	
	@Bridge
	public static native void glBindRenderbuffer(int target, int renderbuffer);
	
	@Bridge
	public static native void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer);
	
	@Bridge
	public static native void glRenderbufferStorage(int target, int internalformat, int width, int height);
}
