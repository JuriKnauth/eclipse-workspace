package org.max.gp;

public class Vertex4D {

	double x;
	double y;
	double z;
	double w;

	public Vertex4D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1.0;
	}
	
	public Vertex4D(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vertex4D mult(Matrix4D m) {
		double xn = x*m.get(0, 0) + y*m.get(1, 0) + z*m.get(2, 0) + w*m.get(3, 0);
		double yn = x*m.get(0, 1) + y*m.get(1, 1) + z*m.get(2, 1) + w*m.get(3, 1);
		double zn = x*m.get(0, 2) + y*m.get(1, 2) + z*m.get(2, 2) + w*m.get(3, 2);
		double wn = x*m.get(0, 3) + y*m.get(1, 3) + z*m.get(2, 3) + w*m.get(3, 3);
		return new Vertex4D(xn, yn, zn, wn);
	}
	
	public Vertex4D toNormalizedDeviceCoordinates() {
		return new Vertex4D(x/w, y/w, z/w, 1.0);
	}
	
	public Vertex4D toWindowCoordinates(double x, double y, double w, double h, int n, int f) {
		return new Vertex4D(this.x*w/2.0 + x + w/2.0, -this.y*h/2.0 + y + h/2.0, this.z*(f-n)/2.0 + (f+n)/2.0);
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+","+z+")";
	}

	public static Vertex4D minus(Vertex4D u, Vertex4D v) {
		return new Vertex4D(u.x-v.x, u.y-v.y, u.z-v.z);
	}

	public static Vertex4D cross(Vertex4D u, Vertex4D v) {
		return new Vertex4D(
				u.y*v.z - u.z*v.y,
				u.z*v.x - u.x*v.z,
				u.x*v.y - u.y*v.x);
	}

	public static Vertex4D normalize(Vertex4D v) {
		double length = Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z);
		return new Vertex4D(v.x/length, v.y/length, v.z/length);
	}

	public static double dot(Vertex4D u, Vertex4D v) {
		return u.x*v.x + u.y*v.y + u.z*v.z;
	}
	
	public static Vertex4D interpolate(Vertex4D v0, Vertex4D v1, Vertex4D v2, double a0, double a1, double a2) {
		return plus(smult(a0, v0), smult(a1, v1), smult(a2, v2));
	}

	private static Vertex4D plus(Vertex4D v0, Vertex4D v1, Vertex4D v2) {
		return new Vertex4D(v0.x + v1.x + v2.x, v0.y + v1.y + v2.y, v0.z + v1.z + v2.z);
	}

	private static Vertex4D smult(double a, Vertex4D v) {
		return new Vertex4D(a*v.x, a*v.y, a*v.z);
	}
}