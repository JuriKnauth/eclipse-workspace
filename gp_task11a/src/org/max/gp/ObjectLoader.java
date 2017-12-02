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
					String[] normalsArray = line.split(" ");
					Vertex4D v = new Vertex4D(Double.parseDouble(normalsArray[1]), Double.parseDouble(normalsArray[2]), Double.parseDouble(normalsArray[3]));
					normals.add(v);
				}
				if (line.startsWith("f ")) {
					String[] triangleArray = line.split(" ");
					Polygon polygon = new Polygon();
					for (int i=1;i<=3;i++) {
						int vertexIndex = Integer.parseInt(triangleArray[i].split("/")[0]) - 1;
						int normalsIndex = Integer.parseInt(triangleArray[i].split("/")[2]) - 1;
						polygon.add(vertices.get(vertexIndex));
						polygon.addN(normals.get(normalsIndex));
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
	
	public static List<Polygon> createSphere(int level){
		List<Polygon> polygons = new ArrayList<Polygon>();
		List<Vertex4D> vertices = new ArrayList<Vertex4D>();
		
		double t = (1.0 + Math.sqrt(5.0) / 2.0);
		
		vertices.add(Vertex4D.normalize(new Vertex4D(-1,  t,  0)));
		vertices.add(Vertex4D.normalize(new Vertex4D( 1,  t,  0)));
		vertices.add(Vertex4D.normalize(new Vertex4D(-1, -t,  0)));
		vertices.add(Vertex4D.normalize(new Vertex4D( 1, -t,  0)));
		
		vertices.add(Vertex4D.normalize(new Vertex4D( 0, -1,  t)));
		vertices.add(Vertex4D.normalize(new Vertex4D( 0,  1,  t)));
		vertices.add(Vertex4D.normalize(new Vertex4D( 0, -1, -t)));
		vertices.add(Vertex4D.normalize(new Vertex4D( 0,  1, -t)));
		
		vertices.add(Vertex4D.normalize(new Vertex4D( t,  0, -1)));
		vertices.add(Vertex4D.normalize(new Vertex4D( t,  0,  1)));
		vertices.add(Vertex4D.normalize(new Vertex4D(-t,  0, -1)));
		vertices.add(Vertex4D.normalize(new Vertex4D(-t,  0,  1)));
		
		Polygon p = new Polygon();	p.add(vertices.get(  0 )); p.add(vertices.get( 11 )); p.add(vertices.get(  5 )); 	polygons.add(p);
		p = new Polygon(); 			p.add(vertices.get(  0 )); p.add(vertices.get(  5 )); p.add(vertices.get(  1 )); 	polygons.add(p);
		p = new Polygon(); 			p.add(vertices.get(  0 )); p.add(vertices.get(  1 )); p.add(vertices.get(  7 ));	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  0 )); p.add(vertices.get(  7 )); p.add(vertices.get( 10 ));	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  0 )); p.add(vertices.get( 10 )); p.add(vertices.get( 11 )); 	polygons.add(p);
		
		p = new Polygon();			p.add(vertices.get(  1 )); p.add(vertices.get(  5 )); p.add(vertices.get(  9 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  5 )); p.add(vertices.get( 11 )); p.add(vertices.get(  4 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get( 11 )); p.add(vertices.get( 10 )); p.add(vertices.get(  2 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get( 10 )); p.add(vertices.get(  7 )); p.add(vertices.get(  6 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  7 )); p.add(vertices.get(  1 )); p.add(vertices.get(  8 )); 	polygons.add(p);
		
		p = new Polygon();			p.add(vertices.get(  3 )); p.add(vertices.get(  9 )); p.add(vertices.get(  4 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  3 )); p.add(vertices.get(  4 )); p.add(vertices.get(  2 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  3 )); p.add(vertices.get(  2 )); p.add(vertices.get(  6 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  3 )); p.add(vertices.get(  6 )); p.add(vertices.get(  8 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  3 )); p.add(vertices.get(  8 )); p.add(vertices.get(  9 )); 	polygons.add(p);
		
		p = new Polygon();			p.add(vertices.get(  4 )); p.add(vertices.get(  9 )); p.add(vertices.get(  5 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  2 )); p.add(vertices.get(  4 )); p.add(vertices.get( 11 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  6 )); p.add(vertices.get(  2 )); p.add(vertices.get( 10 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  8 )); p.add(vertices.get(  6 )); p.add(vertices.get(  7 )); 	polygons.add(p);
		p = new Polygon();			p.add(vertices.get(  9 )); p.add(vertices.get(  8 )); p.add(vertices.get(  1 )); 	polygons.add(p);
		
		for (int i = 0; i < level; i++) {
			List<Polygon> polygonsRefined = new ArrayList<Polygon>();
			for (Polygon polygon : polygons) {

				Vertex4D v0 = polygon.get(0);
				Vertex4D v1 = polygon.get(1);
				Vertex4D v2 = polygon.get(2);

				Vertex4D a = getMiddlePoint(v0, v1);
				Vertex4D b = getMiddlePoint(v1, v2);
				Vertex4D c = getMiddlePoint(v2, v0);

				Polygon p1 = new Polygon();
				p1.add(v0);
				p1.add(a);
				p1.add(c);
				polygonsRefined.add(p1);
				
				p1 = new Polygon();
				p1.add(v1);
				p1.add(b);
				p1.add(a);
				polygonsRefined.add(p1);
				
				p1 = new Polygon();
				p1.add(v2);
				p1.add(c);
				p1.add(b);
				polygonsRefined.add(p1);
				
				p1 = new Polygon();
				p1.add(a);
				p1.add(b);
				p1.add(c);
				polygonsRefined.add(p1);
				
			}
			
			polygons = polygonsRefined;
		}
		
		for(Polygon polygon:polygons){
			for(int i = 0; i < 3; i++){
				polygon.addN(polygon.get(i));
			}
		}
		
		return polygons;
	}
	
	private static Vertex4D getMiddlePoint(Vertex4D u, Vertex4D v) {
		return Vertex4D.normalize(new Vertex4D (0.5*(u.x+v.x), 0.5*(u.y+v.y), 0.5*(u.z+v.z)));
	}

}
