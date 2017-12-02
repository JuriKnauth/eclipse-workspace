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
import java.util.ArrayList;
import java.util.List;
import java.math.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * implement projective and orthographic projection
 */
public class Pipeline01 {

	private static JPanel canvas;
	
	private static double scale = 1.0;
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
//		List<Polygon> polygons = new ArrayList<>();
//		Polygon polygon = new Polygon();
//		polygon.add(new Vertex4D(0, 200, 0));
//		polygon.add(new Vertex4D(100, 100, 0));
//		polygon.add(new Vertex4D(30, 100, 0));
//		polygon.add(new Vertex4D(50, -200, 0));
//		polygon.add(new Vertex4D(-50, -200, 0));
//		polygon.add(new Vertex4D(-30, 100, 0));
//		polygon.add(new Vertex4D(-100, 100, 0));
//		polygons.add(polygon);
//		polygon = new Polygon();
//		polygon.add(new Vertex4D(0, 200, 0));
//		polygon.add(new Vertex4D(0, 100, 100));
//		polygon.add(new Vertex4D(0, 100, 30));
//		polygon.add(new Vertex4D(0, -200, 50));
//		polygon.add(new Vertex4D(0, -200, -50));
//		polygon.add(new Vertex4D(0, 100, -30));
//		polygon.add(new Vertex4D(0, 100, -100));
//		polygons.add(polygon);
//		polygon = new Polygon();
//		polygon.add(new Vertex4D(100, 100, 0));
//		polygon.add(new Vertex4D(0, 100, 100));
//		polygon.add(new Vertex4D(-100, 100, 0));
//		polygon.add(new Vertex4D(0, 100, -100));
//		polygons.add(polygon);
		

		
		List<Polygon> polygons = new ArrayList<>();
		Polygon polygon = new Polygon(Color.RED);
		polygon.add(new Vertex4D(0, 200, 0));
		polygon.add(new Vertex4D(-100, 100, 100));
		polygon.add(new Vertex4D(100, 100, 100));
		polygons.add(polygon);
		polygon = new Polygon(Color.GREEN);
		polygon.add(new Vertex4D(0, 200, 0));
		polygon.add(new Vertex4D(100, 100, -100));
		polygon.add(new Vertex4D(-100, 100, -100));
		polygons.add(polygon);
		polygon = new Polygon(Color.BLUE);
		polygon.add(new Vertex4D(0, 200, 0));
		polygon.add(new Vertex4D(100, 100, 100));
		polygon.add(new Vertex4D(100, 100, -100));
		polygons.add(polygon);
		polygon = new Polygon(Color.ORANGE);
		polygon.add(new Vertex4D(0, 200, 0));
		polygon.add(new Vertex4D(-100, 100, -100));
		polygon.add(new Vertex4D(-100, 100, 100));
		polygons.add(polygon);

		polygon = new Polygon(Color.GREEN);
		polygon.add(new Vertex4D(30, 100, 30));
		polygon.add(new Vertex4D(-50, -200, 50));
		polygon.add(new Vertex4D(50, -200, 50));
		polygons.add(polygon);
		polygon = new Polygon(Color.GREEN);
		polygon.add(new Vertex4D(30, 100, 30));
		polygon.add(new Vertex4D(-30, 100, 30));
		polygon.add(new Vertex4D(-50, -200, 50));
		polygons.add(polygon);
		polygon = new Polygon(Color.BLUE);
		polygon.add(new Vertex4D(30, 100, -30));
		polygon.add(new Vertex4D(50, -200, -50));
		polygon.add(new Vertex4D(-50, -200, -50));
		polygons.add(polygon);
		polygon = new Polygon(Color.BLUE);
		polygon.add(new Vertex4D(30, 100, -30));
		polygon.add(new Vertex4D(-50, -200, -50));
		polygon.add(new Vertex4D(-30, 100, -30));
		polygons.add(polygon);
		polygon = new Polygon(Color.ORANGE);
		polygon.add(new Vertex4D(30, 100, 30));
		polygon.add(new Vertex4D(50, -200, 50));
		polygon.add(new Vertex4D(50, -200, -50));
		polygons.add(polygon);
		polygon = new Polygon(Color.ORANGE);
		polygon.add(new Vertex4D(30, 100, 30));
		polygon.add(new Vertex4D(50, -200, -50));
		polygon.add(new Vertex4D(30, 100, -30));
		polygons.add(polygon);
		polygon = new Polygon(Color.RED);
		polygon.add(new Vertex4D(-30, 100, 30));
		polygon.add(new Vertex4D(-50, -200, -50));
		polygon.add(new Vertex4D(-50, -200, 50));
		polygons.add(polygon);
		polygon = new Polygon(Color.RED);
		polygon.add(new Vertex4D(-30, 100, 30));
		polygon.add(new Vertex4D(-30, 100, -30));
		polygon.add(new Vertex4D(-50, -200, -50));
		polygons.add(polygon);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLayout(new BorderLayout());
		canvas = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
				
				BufferedImage bufferedImage = new BufferedImage(canvas.getWidth(), canvas. getHeight(), BufferedImage.TYPE_INT_RGB);
				
				double[][] zBuffer = new double[canvas.getWidth()][canvas.getHeight()];
				for(int i=0; i < canvas.getWidth(); i++){
					for(int j=0; j < canvas.getHeight(); j++){
						zBuffer[i][j] = Double.MAX_VALUE;	
					}
				}
				
				for (Polygon polygon : polygons){
					rasterPolygon(polygon, bufferedImage, zBuffer);
				}
				
				g2d.drawImage(bufferedImage, 0, 0, null);
				
//				for (Polygon polygon : polygons) {
//					g2d.draw(getShape(polygon));
//				}
			}

			private void rasterPolygon(Polygon polygon, BufferedImage bufferedImage, double[][] zBuffer) {
				
				Vertex4D v0 = vertexShader(polygon.get(0));
				Vertex4D v1 = vertexShader(polygon.get(1));
				Vertex4D v2 = vertexShader(polygon.get(2));
				
				int xMin = (int) Math.min(Math.min(v0.x, v1.x), v2.x);
				if (xMin < 0){
					xMin = 0;
				}
				int xMax = (int) Math.max(Math.max(v0.x, v1.x), v2.x);
				if (xMax > canvas.getWidth()){
					xMax = canvas.getWidth();
				}
				
				int yMin = (int) Math.min(Math.min(v0.y, v1.y), v2.y);
				if (yMin < 0){
					yMin = 0;
				}
				
				int yMax = (int) Math.max(Math.max(v0.y, v1.y), v2.y);
				if (yMax > canvas.getHeight()){
					yMax = canvas.getHeight();
				}
				
				double triangleArea = (v0.x - v1.x) * (v2.y - v1.y) - (v0.y - v1.y) * (v2.x - v1.x);
				
				for(int x = xMin; x < xMax; x++){
					for(int y = yMin; y < yMax; y++){
						double e0 = (x-v1.x)*(v2.y-v1.y)-(y-v1.y)*(v2.x-v1.x);
						double e1 = (x-v2.x)*(v0.y-v2.y)-(y-v2.y)*(v0.x-v2.x);
						double e2 = (x-v0.x)*(v1.y-v0.y)-(y-v0.y)*(v1.x-v0.x);
						
						if(e0>=0 && e1>=0 && e2>=0 || e0<=0 && e1<=0 && e2<=0){
							double a0 = e0 / triangleArea;
							double a1 = e1 / triangleArea;
							double a2 = e2 / triangleArea;
							double z = a0*v0.z + a1*v1.z + a2*v2.z;
							if(zBuffer[x][y] > z){
								
								Color c = new Color((int)(a0*255), (int)(a1*255), (int)(a2*255));
								
								
								bufferedImage.setRGB(x, y, c.getRGB());
								zBuffer[x][y] = z;
							}
							
						}
						
						
					}
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
					case KeyEvent.VK_P:
						projection = !projection;
						break;
					case KeyEvent.VK_0:
						scale = 1.0;
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

	private static Vertex4D vertexShader(Vertex4D objectCoordinates) {
		Matrix4D translationMatrix = Matrix4D.getTranslationMatrix(xtrans, ytrans, ztrans);
		Matrix4D scalingMatrix = Matrix4D.getScalingMatrix(scale);
		Matrix4D rotationMatrix = rotationAxis.getRotationMatrix();
		Matrix4D modelMatrix = rotationMatrix.mult(scalingMatrix).mult(translationMatrix);
		
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
}
