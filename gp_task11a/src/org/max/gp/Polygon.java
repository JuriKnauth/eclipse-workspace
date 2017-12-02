package org.max.gp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Polygon extends ArrayList<Vertex4D> {
	
	List<Vertex4D> normales = new ArrayList<Vertex4D>();

	private static final long serialVersionUID = 1L;
	Color color;;
	
	public Polygon() {
		this.color = Color.lightGray;
	}
	
	public Polygon(Color color) {
		this.color = color;
	}
	
	public void addN(Vertex4D n){
		normales.add(n);
	}
	
	public Vertex4D getN(int i){
		
		return normales.get(i);
	}
	
	
}
