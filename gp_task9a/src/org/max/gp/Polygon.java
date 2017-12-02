package org.max.gp;

import java.awt.Color;
import java.util.ArrayList;

public class Polygon extends ArrayList<Vertex4D> {

	private Color color;
	
	public Polygon() {
		this(Color.LIGHT_GRAY);
	}
	
	public Polygon(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}

	private static final long serialVersionUID = 1L;

}