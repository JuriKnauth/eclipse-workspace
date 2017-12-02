package org.max.gp;

import java.awt.Color;
import java.util.ArrayList;

public class Polygon extends ArrayList<Vertex4D> {

	private static final long serialVersionUID = 1L;
	Color color;;
	
	public Polygon() {
		this.color = Color.GREEN;
	}
	
	public Polygon(Color color) {
		this.color = color;
	}
}
