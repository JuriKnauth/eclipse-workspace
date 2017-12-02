package org.max.gp;

public class Matrix4D {
	private double[][] matrix;
	
	public Matrix4D(double[][] matrix) {
		this.matrix = matrix;
	}
	
	public double get(int i, int j) {
		return matrix[j][i];
	}
	
	public static Matrix4D getTranslationMatrix(double vx, double vy, double vz) {
		double[][] matrix = {{1.0, 0.0, 0.0, vx }, 
				             {0.0, 1.0, 0.0, vy },
				             {0.0, 0.0, 1.0, vz },
				             {0.0, 0.0, 0.0, 1.0}};
		return new Matrix4D(matrix);
	}
	
	public static Matrix4D getScalingMatrix(double sx, double sy, double sz) {
		double[][] matrix = {{sx,  0.0, 0.0, 0.0}, 
				             {0.0, sy,  0.0, 0.0},
				             {0.0, 0.0, sz,  0.0},
				             {0.0, 0.0, 0.0, 1.0}};
		return new Matrix4D(matrix);
	}
	
	public static Matrix4D getScalingMatrix(double scale) {
		return getScalingMatrix(scale, scale, scale);
	}
	
	public static Matrix4D getPerspectiveProjectionMatrix(double l, double r, double b, double t, double n, double f) {
		double[][] matrix = {{2.0*n/(r-l),  0.0,         (r+l)/(r-l),  0.0}, 
				             {0.0,          2.0*n/(t-b), (t+b)/(t-b),  0.0},
				             {0.0,          0.0,         -(f+n)/(f-n), -2.0*f*n/(f-n)},
				             {0.0,          0.0,         -1.0,         0.0}};
		return new Matrix4D(matrix);
	}
	
	/*
	 *  assumes l=-r and b=-t
	 */
	public static Matrix4D getPerspectiveProjectionMatrix(double r, double t, double n, double f) {
		double[][] matrix = {{n/r, 0.0, 0.0,          0.0}, 
				             {0.0, n/t, 0.0,          0.0},
				             {0.0, 0.0, -(f+n)/(f-n), -2.0*f*n/(f-n)},
				             {0.0, 0.0, -1.0,         0.0}};
		return new Matrix4D(matrix);
	}
	
	public static Matrix4D getOrthographicProjectionMatrix(double l, double r, double b, double t, double n, double f) {
		double[][] matrix = {{2.0/(r-l), 0.0,      0.0,        -(r+l)/(r-l)}, 
				             {0.0,      2.0/(t-b), 0.0,        -(t+b)/(t-b)},
				             {0.0,      0.0,       -2.0/(f-n), -(f+n)/(f-n)},
				             {0.0,      0.0,       0.0,         1.0}};
		return new Matrix4D(matrix);
	}
	
	/*
	 *  assumes l=-r and b=-t
	 */
	public static Matrix4D getOrthographicProjectionMatrix(double r, double t, double n, double f) {
		double[][] matrix = {{1.0/r, 0.0,   0.0,        0.0}, 
				             {0.0,   1.0/t, 0.0,        0.0},
				             {0.0,   0.0,   -2.0/(f-n), -(f+n)/(f-n)},
				             {0.0,   0.0,   0.0,        1.0}};
		return new Matrix4D(matrix);
	}

	public Matrix4D mult(Matrix4D matrix) {
		double[][] nmatrix = new double[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					nmatrix[j][i] += this.get(i, k) * matrix.get(k, j);
				}
			}
		}

		return new Matrix4D(nmatrix);
	}
}