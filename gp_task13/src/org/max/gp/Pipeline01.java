package org.max.gp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Pipeline01 {

	private static JPanel canvas;
	
	private static double scale = 100.0;
	private static double xtrans = 0.0;
	private static double ytrans = 0.0;
	private static double ztrans = 0.0;
	
	private static double ztransView = -500.0;

	private static Quaternion rotationAxis = new Quaternion(1.0, 0.0, 0.0, 0.0);
	
	private static boolean projection = false;
	private static boolean bfcbot = false;
	private static boolean bfcoff = true;
	
	private static boolean ambbut = false;
	private static boolean diffbut = true;
	private static boolean specbut = false;

	private static boolean  meshbut = false;
	private static boolean flatbut = true;;
	
	private static boolean ambbutboost = false;
		
	private static int teslevel = 4;
	
	public static void main(String[] args) {
		//List<Polygon> polygons = ObjectLoader.createIcoSphere(teslevel);
		//List<Polygon> polygons = ObjectLoader.loadOBJ("object.obj");
		//List<Polygon> polygons = ObjectLoader.loadOBJ("Sword4.obj");
		List<Polygon> polygons = ObjectLoader.loadOBJ("WomanJava.obj");
		//List<Polygon> polygons = ObjectLoader.loadOBJ("NovaJava2.obj");

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
				
				if(meshbut == true)
				for (Polygon polygon : polygons) {
					g2d.draw(getShape(polygon));
				}
				
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
//					case KeyEvent.VK_NUMPAD2:
//						tes++;
//						break;
//					case KeyEvent.VK_NUMPAD1:
//						tes--;
//						if(tes < 0)
//							{tes = 1;}
//						break;
					case KeyEvent.VK_P:
						projection = !projection;
						break;
					case KeyEvent.VK_B:
						bfcbot = !bfcbot;
						break;
					case KeyEvent.VK_M:
						meshbut = !meshbut;
						break;
					case KeyEvent.VK_L:
						flatbut = !flatbut;
						break;
					case KeyEvent.VK_K:
						ambbutboost = !ambbutboost;
						break;
					case KeyEvent.VK_A:
						ambbut = !ambbut;
						break;
					case KeyEvent.VK_D:
						diffbut = !diffbut;
						break;
					case KeyEvent.VK_S:
						specbut = !specbut;
						break;
					case KeyEvent.VK_0:
						scale = 200.0;
						xtrans = 0.0;
						ytrans = 0.0;
						rotationAxis = new Quaternion(1.0, 0.0, 0.0, 0.0);
						break;
					case KeyEvent.VK_ESCAPE:
						frame.dispose();
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
		Vertex4D worldCoordinates;
		if(flatbut == true) {
			worldCoordinates = getWorldCoordinates(objectCoordinates);}
		else {
			worldCoordinates = objectCoordinates.mult(modelMatrix);
		}
		
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
		
		//
		Vertex4D w0 = getWorldCoordinates(polygon.get(0));
		Vertex4D w1 = getWorldCoordinates(polygon.get(1));
		Vertex4D w2 = getWorldCoordinates(polygon.get(2));
		
		Vertex4D w10 = Vertex4D.minus(w1, w0);
		Vertex4D w20 = Vertex4D.minus(w2, w0);
		
		Vertex4D cross2 = Vertex4D.cross(w10, w20);
		
		Vertex4D normal2 = Vertex4D.normal2(cross2);
		//
		
		// get normal default
		Vertex4D v0w = polygon.get(0).mult(getModelMatrix());
		Vertex4D v1w = polygon.get(1).mult(getModelMatrix());
		Vertex4D v2w = polygon.get(2).mult(getModelMatrix());
		Vertex4D v0wv1w = Vertex4D.minus(v0w, v1w);
		Vertex4D v0wv2w = Vertex4D.minus(v0w, v2w);
		Vertex4D cross = Vertex4D.cross(v0wv1w, v0wv2w);
		Vertex4D norm = Vertex4D.normalize(cross);
		// get normals
		Vertex4D n0 = null;
		Vertex4D n1 = null;
		Vertex4D n2 = null;
		if (polygon.hasNormals()) {
			n0 = polygon.getN(0).mult(getModelMatrix());
			n1 = polygon.getN(1).mult(getModelMatrix());
			n2 = polygon.getN(2).mult(getModelMatrix());
		}

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

				if(bfcbot == false)
				{if (e0<=0 && e1<=0 && e2<=0) {
					bfcoff = true;}
				else{
					bfcoff = false;}}
				
				
				if(e0>=0 && e1>=0 && e2>=0 || bfcoff){
					double a0 = e0 / triangleArea;
					double a1 = e1 / triangleArea;
					double a2 = e2 / triangleArea;
					double pixelZ = a0*v0.z + a1*v1.z + a2*v2.z;
										
					if (zBuffer[x][y] > pixelZ) {
						Vertex4D fragmentPos;
						if(flatbut == true) {
							fragmentPos = Vertex4D.interpolate(w0,w1,w2,a0,a1,a2);
							img.setRGB(x, y, fragmentShader(polygon.color, normal2, fragmentPos));
							zBuffer[x][y] = pixelZ;
						}
						else
						{
							fragmentPos = Vertex4D.interpolate(v0w, v1w, v2w, a0, a1, a2);
							if (polygon.hasNormals()) {
								norm = Vertex4D.normalize(Vertex4D.interpolate(n0, n1, n2, a0, a1, a2));
							}
							img.setRGB(x, y, fragmentShader(polygon.color, norm, fragmentPos));
							zBuffer[x][y] = pixelZ;
						}
					}
				}
			}
		}
	}
	
	private static Shape getShape(Polygon polygon) {
		Path2D.Double path = new Path2D.Double();
		for (int i = 0; i < polygon.size(); i++) {
			Vertex4D vertex = polygon.get(i);
			vertex = vertexShader(vertex);
			if (i == 0) {
				path.moveTo(vertex.x, vertex.y);
			} else {
				path.lineTo(vertex.x, vertex.y);
			}
		}
		path.closePath();
		return path;
	}
	
	private static Vertex4D getWorldCoordinates(Vertex4D objectCoordinates) {
		Matrix4D translationMatrix = Matrix4D.getTranslationMatrix(xtrans, ytrans, ztrans);
		Matrix4D scalingMatrix = Matrix4D.getScalingMatrix(scale);
		Matrix4D rotationMatrix = rotationAxis.getRotationMatrix();
		Matrix4D modelMatrix = rotationMatrix.mult(scalingMatrix).mult(translationMatrix);
		
		Vertex4D worldCoordinates = objectCoordinates.mult(modelMatrix);
		return worldCoordinates;
	}
	
	private static int fragmentShader(Color color, Vertex4D norm, Vertex4D fragmentPos) {
		double red = color.getRed();
		double green = color.getGreen();
		double blue = color.getBlue();
		double lightColor = 1.0;
		double ambient;
		if (ambbutboost == true) {
			 ambient = 0.6;
		}else{
			ambient = 0.06;}

		double specular = 0.0;
		double shaded = 0.0;
		
		Vertex4D camPos = new Vertex4D (0, 0, 500);
			
		Vertex4D lightPos = new Vertex4D(-200.0, 100.0, 400.0, 0);
		
		Vertex4D lightDir = Vertex4D.normalize(Vertex4D.minus(lightPos, fragmentPos));
		double diffuseCos = Math.max(Vertex4D.dot(norm, lightDir), 0.0);
		double diffuseStrength = 0.5;
		double diffuse = diffuseStrength * diffuseCos * lightColor;
		
		Vertex4D refDir = Vertex4D.reflect(Vertex4D.negate(lightDir), norm);
		Vertex4D camDir = Vertex4D.normalize(Vertex4D.minus(camPos,fragmentPos));
		
		double specCos = Math.max(Vertex4D.dot(refDir, camDir), 0.0);
		double specStrength = 7;
		double spec = Math.pow(specCos, 32) * specStrength;
		
		int ambint = (ambbut) ? 1 : 0;
		int difint = (diffbut) ? 1 : 0;
		int specint = (specbut) ? 1 : 0;
		
		shaded= shaded + ambient * ambint + diffuse * difint + spec*specint;
		
		red = Math.min(shaded*red, 255);
		green = Math.min(shaded*green, 255);
		blue = Math.min(shaded*blue, 255);
		return new Color((int) red, (int) green, (int) blue).getRGB();
	}
}
