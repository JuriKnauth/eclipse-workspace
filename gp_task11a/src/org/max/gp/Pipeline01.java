package org.max.gp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Pipeline01 {

	private static JPanel canvas;
	
	private static double scale = 200.0;
	private static double xtrans = 0.0;
	private static double ytrans = 0.0;
	private static double ztrans = 0.0;
	
	private static double ztransView = -500.0;

	private static Quaternion rotationAxis = new Quaternion(1.0, 0.0, 0.0, 0.0);
	
	/*
	 * true  := perspective
	 * false := ortographic
	 */
	private static boolean projection = false;

	public static void main(String[] args) {
		List<Polygon> polygons = ObjectLoader.loadOBJ("object.obj");
//		List<Polygon> polygons = ObjectLoader.createSphere(1);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLayout(new BorderLayout());
		canvas = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(Color.black);
				g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
				
				BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				double[][] zBuffer = new double[img.getWidth()][img.getHeight()];
				// initialize array with extremely far away depths
				for (int q = 0; q < img.getWidth(); q++) {
					for (int r = 0; r < img.getHeight(); r++) {
						zBuffer[q][r] = Double.POSITIVE_INFINITY;
					}
				}
				
				for (Polygon polygon : polygons) {
					rasterPolygon(img, zBuffer, polygon);
				}
				
				g2d.drawImage(img, 0, 0, null);
			}
		};
		
		frame.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				scale = scale * Math.pow(1.1, e.getWheelRotation());
				canvas.repaint();
			}
		});
		
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				double transOffset = 10.0;
				int degOffset = 3;
				Vertex4D axis = null;
				if (arg0.isAltDown())
					degOffset = -degOffset;
				
				switch (arg0.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						xtrans -= transOffset;
						break;
					case KeyEvent.VK_RIGHT:
						xtrans += transOffset;
						break;
					case KeyEvent.VK_UP:
						ytrans += transOffset;
						break;
					case KeyEvent.VK_DOWN:
						ytrans -= transOffset;
						break;
					case KeyEvent.VK_1:
						axis = new Vertex4D(1.0, 0.0, 0.0);
						break;
					case KeyEvent.VK_2:
						axis = new Vertex4D(0.0, 1.0, 0.0);
						break;
					case KeyEvent.VK_3:
						axis = new Vertex4D(0.0, 0.0, 1.0);
						break;
					case KeyEvent.VK_P:
						projection = !projection;
						break;
					case KeyEvent.VK_0:
						scale = 200.0;
						xtrans = 0.0;
						ytrans = 0.0;
						rotationAxis = new Quaternion(1.0, 0.0, 0.0, 0.0);
						break;
				}
				
				if (axis != null) {
					rotationAxis = Quaternion.getRotationQuaternion(axis, degOffset).mult(rotationAxis);
				}
				canvas.repaint();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
		
		frame.add(canvas, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	private static Vertex4D vertexShader(Vertex4D objectCoordinates) {
		Matrix4D modelMatrix = getModelMatrix();
		
		Vertex4D worldCoordinates = objectCoordinates.mult(modelMatrix);
		
		Matrix4D viewMatrix = Matrix4D.getTranslationMatrix(0.0, 0.0, ztransView);
		
		Vertex4D eyeCoordinates = worldCoordinates.mult(viewMatrix);

		Matrix4D perspectiveMatrix;
		if (projection) {
			perspectiveMatrix = Matrix4D.getPerspectiveProjectionMatrix(400.0, 300.0, 200, 800);
		} else {
			perspectiveMatrix = Matrix4D.getOrthographicProjectionMatrix(400.0, 300.0, 200, 800);
		}
		Vertex4D clipCoordinates = eyeCoordinates.mult(perspectiveMatrix);

		// clipping would happen here
		
		Vertex4D normalizedDeviceCoordinates = clipCoordinates.toNormalizedDeviceCoordinates();
		Vertex4D windowCoordinates = normalizedDeviceCoordinates.toWindowCoordinates(0.0, 0.0, 800.0, 600.0, 200, 800);
		return windowCoordinates;
	}

	private static Matrix4D getModelMatrix() {
		Matrix4D translationMatrix = Matrix4D.getTranslationMatrix(xtrans, ytrans, ztrans);
		Matrix4D scalingMatrix = Matrix4D.getScalingMatrix(scale);
		Matrix4D rotationMatrix = rotationAxis.getRotationMatrix();
		Matrix4D modelMatrix = rotationMatrix.mult(scalingMatrix).mult(translationMatrix);
		return modelMatrix;
	}
	
	// point
	private static void rasterPolygon(BufferedImage img, double[][] zBuffer, Polygon polygon) {
		Vertex4D v0 = vertexShader(polygon.get(0));
		Vertex4D v1 = vertexShader(polygon.get(1));
		Vertex4D v2 = vertexShader(polygon.get(2));
		
		// get normal
		Vertex4D v0w = polygon.get(0).mult(getModelMatrix());
		Vertex4D v1w = polygon.get(1).mult(getModelMatrix());
		Vertex4D v2w = polygon.get(2).mult(getModelMatrix());
		Vertex4D v0wv1w = Vertex4D.minus(v0w, v1w);
		Vertex4D v0wv2w = Vertex4D.minus(v0w, v2w);
		Vertex4D cross = Vertex4D.cross(v0wv1w, v0wv2w);
		Vertex4D norm = Vertex4D.normalize(cross);
		
		Vertex4D n0 = polygon.getN(0).mult(getModelMatrix());
		Vertex4D n1 = polygon.getN(1).mult(getModelMatrix());
		Vertex4D n2 = polygon.getN(2).mult(getModelMatrix());

		int minX = (int) Math.max(0, Math.ceil(Math.min(v0.x, Math.min(v1.x, v2.x))));
		int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v0.x, Math.max(v1.x, v2.x))));
		int minY = (int) Math.max(0, Math.ceil(Math.min(v0.y, Math.min(v1.y, v2.y))));
		int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v0.y, Math.max(v1.y, v2.y))));
		
		double triangleArea = (v0.x - v1.x) * (v2.y - v1.y) - (v0.y - v1.y) * (v2.x - v1.x);
	
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				double e0 = (x - v1.x) * (v2.y - v1.y) - (y - v1.y) * (v2.x - v1.x);
				double e1 = (x - v2.x) * (v0.y - v2.y) - (y - v2.y) * (v0.x - v2.x);
				double e2 = (x - v0.x) * (v1.y - v0.y) - (y - v0.y) * (v1.x - v0.x);

				if ((e0 >= 0 && e1 >= 0 && e2 >= 0) || (e0 <= 0 && e1 <= 0 && e2 <= 0)) {
					double a0 = e0 / triangleArea;
					double a1 = e1 / triangleArea;
					double a2 = e2 / triangleArea;
					double pixelZ = a0*v0.z + a1*v1.z + a2*v2.z;
					if (zBuffer[x][y] > pixelZ) {
						Vertex4D fragmentPos = Vertex4D.interpolate(v0w, v1w, v2w, a0, a1, a2);
						Vertex4D normal = Vertex4D.normalize(Vertex4D.interpolate(n0, n1, n2, a0, a1, a2));
						img.setRGB(x, y, fragmentShader(polygon.color, normal, fragmentPos));
						zBuffer[x][y] = pixelZ;
					}
				}
			}
		}
	}
	
	private static int fragmentShader(Color color, Vertex4D norm, Vertex4D fragmentPos) {
		double red = color.getRed();
		double green = color.getGreen();
		double blue = color.getBlue();
		double lightColor = 1.0;
		
		Vertex4D lightPos = new Vertex4D(0.0, 100.0, 300.0, 0);

		Vertex4D lightDir = Vertex4D.normalize(Vertex4D.minus(lightPos, fragmentPos));
		double pointCos = Math.max(Vertex4D.dot(norm, lightDir), 0.0);
		double pointStrength = 0.5;
		double point = pointStrength * pointCos * lightColor;
		
		red = Math.min(point*red, 255);
		green = Math.min(point*green, 255);
		blue = Math.min(point*blue, 255);
		return new Color((int) red, (int) green, (int) blue).getRGB();
	}
}
