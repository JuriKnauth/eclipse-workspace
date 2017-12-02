package org.max.gp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

	public static List<Polygon> loadObj(String filename) {
		List<Polygon> result = new ArrayList<Polygon>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			List<Vertex4D> vertices = new ArrayList<Vertex4D>();

			String line;
			while ((line = br.readLine()) != null) {

				if (line.startsWith("v ")) {
					String[] vertexArray = line.split(" ");
					double x = Double.parseDouble(vertexArray[1]);
					double y = Double.parseDouble(vertexArray[2]);
					double z = Double.parseDouble(vertexArray[3]);
					Vertex4D vertex = new Vertex4D(x, y, z);
					vertices.add(vertex);
				}

				if (line.startsWith("f ")) {
					Polygon triangle = new Polygon();
					String[] triangleArray = line.split(" ");
					String[] v0Array = triangleArray[1].split("/");
					String[] v1Array = triangleArray[2].split("/");
					String[] v2Array = triangleArray[3].split("/");
					Vertex4D v0 = vertices.get(Integer.parseInt(v0Array[0]) - 1);
					Vertex4D v1 = vertices.get(Integer.parseInt(v1Array[0]) - 1);
					Vertex4D v2 = vertices.get(Integer.parseInt(v2Array[0]) - 1);
					triangle.add(v0);
					triangle.add(v1);
					triangle.add(v2);
					result.add(triangle);
				}

			}

			br.close();
		} catch (Exception e) {

		}

		return result;
	}

}