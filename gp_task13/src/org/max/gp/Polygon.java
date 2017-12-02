package org.max.gp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Polygon extends ArrayList<Vertex4D> {

	private static final long serialVersionUID = 1L;
	Color color;
	private List<Vertex4D> normals;
	
	public Polygon() {
		this(Color.ORANGE);
	}
	
	public Polygon(Color color) {
		this.color = color;
		normals = new ArrayList<Vertex4D>();
	}

	public void addN(Vertex4D n) {
		normals.add(n);
	}
	
	public Vertex4D getN(int i) {
		return normals.get(i);
	}
	
	public boolean hasNormals() {
		return normals.size() == this.size();
	}
}
