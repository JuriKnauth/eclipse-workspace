package org.max.gp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

	public static List<Polygon> loadOBJ(String file) {
		List<Polygon> polygons = new ArrayList<Polygon>();
		List<Vertex4D> vertices = new ArrayList<Vertex4D>();
		List<Vertex4D> normals = new ArrayList<Vertex4D>();
		
		System.out.println(String.format("Loading object %s.", file));
		long startTime = System.currentTimeMillis();
		
		String line;
		try {
			InputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.startsWith("v ")) {
					String[] vertexArray = line.split(" ");
					Vertex4D v = new Vertex4D(Double.parseDouble(vertexArray[1]), Double.parseDouble(vertexArray[2]), Double.parseDouble(vertexArray[3]));
					vertices.add(v);
				}
				if (line.startsWith("vn ")) {
					String[] vertexArray = line.split(" ");
					Vertex4D v = new Vertex4D(Double.parseDouble(vertexArray[1]), Double.parseDouble(vertexArray[2]), Double.parseDouble(vertexArray[3]));
					normals.add(v);
				}
				if (line.startsWith("f ")) {
					String[] triangleArray = line.split(" ");
					Polygon polygon = new Polygon();
					for (int i=1;i<=3;i++) {
						String[] vertexArray = triangleArray[i].split("/");
						int vertexIndex = Integer.parseInt(vertexArray[0]) - 1;
						polygon.add(vertices.get(vertexIndex));
						int normalIndex = Integer.parseInt(vertexArray[2]) - 1;
						polygon.addN(normals.get(normalIndex));
					}
					polygons.add(polygon);
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("wooops");
		}
		
		System.out.println(String.format("Loading object %s took %d ms.", file, System.currentTimeMillis() - startTime));

		return polygons;
	}
	
	// http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
		public static List<Polygon> createIcoSphere(int level) {
			// create 12 vertices of a icosahedron
			List<Vertex4D> icosahedron = new ArrayList<Vertex4D>();
			double t = (1.0 + Math.sqrt(5.0)) / 2.0;
			icosahedron.add(Vertex4D.normalize(new Vertex4D(-1,  t, 0)));
			icosahedron.add(Vertex4D.normalize(new Vertex4D( 1,  t, 0)));
			icosahedron.add(Vertex4D.normalize(new Vertex4D(-1, -t, 0)));
			icosahedron.add(Vertex4D.normalize(new Vertex4D( 1, -t, 0)));
			
			icosahedron.add(Vertex4D.normalize(new Vertex4D( 0, -1,  t)));
			icosahedron.add(Vertex4D.normalize(new Vertex4D( 0,  1,  t)));
			icosahedron.add(Vertex4D.normalize(new Vertex4D( 0, -1, -t)));
			icosahedron.add(Vertex4D.normalize(new Vertex4D( 0,  1, -t)));
			
			icosahedron.add(Vertex4D.normalize(new Vertex4D( t,  0, -1)));
			icosahedron.add(Vertex4D.normalize(new Vertex4D( t,  0,  1)));
			icosahedron.add(Vertex4D.normalize(new Vertex4D(-t,  0, -1)));
			icosahedron.add(Vertex4D.normalize(new Vertex4D(-t,  0,  1)));
			
			// create 20 triangles of the icosahedron
			List<Polygon> faces = new ArrayList<Polygon>();
			// 5 faces around point 0
			Polygon triangle = new Polygon();
			triangle.add(icosahedron.get(0));
			triangle.add(icosahedron.get(11));
			triangle.add(icosahedron.get(5));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(0));
			triangle.add(icosahedron.get(5));
			triangle.add(icosahedron.get(1));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(0));
			triangle.add(icosahedron.get(1));
			triangle.add(icosahedron.get(7));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(0));
			triangle.add(icosahedron.get(7));
			triangle.add(icosahedron.get(10));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(0));
			triangle.add(icosahedron.get(10));
			triangle.add(icosahedron.get(11));
			faces.add(triangle);
	        // 5 adjacent faces 
			triangle = new Polygon();
			triangle.add(icosahedron.get(1));
			triangle.add(icosahedron.get(5));
			triangle.add(icosahedron.get(9));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(5));
			triangle.add(icosahedron.get(11));
			triangle.add(icosahedron.get(4));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(11));
			triangle.add(icosahedron.get(10));
			triangle.add(icosahedron.get(2));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(10));
			triangle.add(icosahedron.get(7));
			triangle.add(icosahedron.get(6));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(7));
			triangle.add(icosahedron.get(1));
			triangle.add(icosahedron.get(8));
			faces.add(triangle);

	        // 5 faces around point 3
			triangle = new Polygon();
			triangle.add(icosahedron.get(3));
			triangle.add(icosahedron.get(9));
			triangle.add(icosahedron.get(4));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(3));
			triangle.add(icosahedron.get(4));
			triangle.add(icosahedron.get(2));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(3));
			triangle.add(icosahedron.get(2));
			triangle.add(icosahedron.get(6));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(3));
			triangle.add(icosahedron.get(6));
			triangle.add(icosahedron.get(8));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(3));
			triangle.add(icosahedron.get(8));
			triangle.add(icosahedron.get(9));
			faces.add(triangle);

	        // 5 adjacent faces 
			triangle = new Polygon();
			triangle.add(icosahedron.get(4));
			triangle.add(icosahedron.get(9));
			triangle.add(icosahedron.get(5));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(2));
			triangle.add(icosahedron.get(4));
			triangle.add(icosahedron.get(11));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(6));
			triangle.add(icosahedron.get(2));
			triangle.add(icosahedron.get(10));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(8));
			triangle.add(icosahedron.get(6));
			triangle.add(icosahedron.get(7));
			faces.add(triangle);
			triangle = new Polygon();
			triangle.add(icosahedron.get(9));
			triangle.add(icosahedron.get(8));
			triangle.add(icosahedron.get(1));
			faces.add(triangle);
			
			for (int i=0; i<level; i++) {
				List<Polygon> faces2 = new ArrayList<Polygon>();
				for (Polygon tri : faces) {
					Vertex4D a = Vertex4D.normalize(getMiddlePoint(tri.get(0), tri.get(1)));
					Vertex4D b = Vertex4D.normalize(getMiddlePoint(tri.get(1), tri.get(2)));
					Vertex4D c = Vertex4D.normalize(getMiddlePoint(tri.get(2), tri.get(0)));
					
					Polygon pol = new Polygon();
					pol.add(tri.get(0));
					pol.add(a);
					pol.add(c);
					faces2.add(pol);
					pol = new Polygon();
					pol.add(tri.get(1));
					pol.add(b);
					pol.add(a);
					faces2.add(pol);
					pol = new Polygon();
					pol.add(tri.get(2));
					pol.add(c);
					pol.add(b);
					faces2.add(pol);
					pol = new Polygon();
					pol.add(a);
					pol.add(b);
					pol.add(c);
					faces2.add(pol);
				}
				faces = faces2;
			}
			
			for (Polygon tri : faces) {
				for (int i=0;i< 3;i++) {
					tri.addN(tri.get(i));
				}
			}
			
			return faces;
		}
		
		private static Vertex4D getMiddlePoint(Vertex4D v1, Vertex4D v2) {
			Vertex4D middle = Vertex4D.smult(0.5, Vertex4D.plus(v1, v2));
			return middle;
		}

}
