package org.max.gp;

public class Quaternion {
	
	double w;
	double x;
	double y;
	double z;

	public Quaternion(double w, double x, double y, double z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Quaternion mult(Quaternion q) {
		double wn = w * q.w - x * q.x - y * q.y - z * q.z;
		double xn = w * q.x + x * q.w + y * q.z - z * q.y;
		double yn = w * q.y - x * q.z + y * q.w + z * q.x;
		double zn = w * q.z + x * q.y - y * q.x + z * q.w;
		return new Quaternion(wn, xn, yn, zn);
	}

	public static Quaternion getRotationQuaternion(Vertex4D axis, double deg) {
		double deghr = 0.5 * deg * Math.PI / 180.0;// Math.toRadians(deg/2);
		Quaternion rotq = new Quaternion(Math.cos(deghr), axis.x * Math.sin(deghr), axis.y * Math.sin(deghr),
				axis.z * Math.sin(deghr));
		return rotq;
	}

	public Matrix4D getRotationMatrix() {
		double[][] matrixArray = {
				{1 - 2 * y * y - 2 * z * z, 2 * x * y - 2 * w * z,     2 * x * z + 2 * w * y,     0.0},
				{2 * x * y + 2 * w * z,     1 - 2 * x * x - 2 * z * z, 2 * y * z - 2 * w * x,     0.0 },
				{2 * x * z - 2 * w * y,     2 * y * z + 2 * w * x,     1 - 2 * x * x - 2 * y * y, 0.0 },
				{0.0,                       0.0,                       0.0,                       1.0}};
		// does not use that we work here with unit quanternions
		// double[][] matrixArray = {
		// { w*w + x*x - y*y - z*z, 2*x*y - 2*w*z, 2*x*z + 2*w*y },
		// { 2*x*y + 2*w*z, w*w - x*x + y*y - z*z, 2*y*z - 2*w*x },
		// { 2*x*z - 2*w*y, 2*y*z + 2*w*x, w*w - x*x - y*y + z*z } };
		return new Matrix4D(matrixArray);
	}

	public void makeUnit() {
		double norm = getNorm();
		w = w / norm;
		x = x / norm;
		y = y / norm;
		z = z / norm;
	}

	private double getNorm() {
		return Math.sqrt(w * w + x * x + y * y + z * z);
	}
}