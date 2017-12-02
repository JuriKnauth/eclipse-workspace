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

	public static Vertex4D minus(Vertex4D u, Vertex4D v) {
			return new Vertex4D(u.x-v.x, u.y-v.y, u.z-v.z);
	}

	public static Vertex4D cross(Vertex4D a, Vertex4D b) {
		return new Vertex4D(((a.y*b.z)-(a.z*b.y)), ((a.z*b.x)-(a.x*b.z)), ((a.x*b.y)-(a.y*b.x)));
	}

	public static Vertex4D normal(Vertex4D n) {
		double length = Math.sqrt(Math.pow(n.x,2)+Math.pow(n.y,2)+Math.pow(n.z,2));
		
		return new Vertex4D(n.x/length,n.y/length,n.z/length);
	}

	public static double dot(Vertex4D u, Vertex4D v) {

		return u.x*v.x+u.y*v.y+u.z*v.z;
	}

	public static Vertex4D interpolate(double a0, double a1, double a2, Vertex4D w0, Vertex4D w1, Vertex4D w2) {
		
		return new Vertex4D(a0*w0.x+a1*w1.x+a2*w2.x,a0*w0.y+a1*w1.y+a2*w2.y,a0*w0.z+a1*w1.z+a2*w2.z);
	}
}