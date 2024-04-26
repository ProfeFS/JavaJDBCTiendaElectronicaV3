package edu.cesurformacion.programacion.jdbc.model;

import java.time.LocalDate;

public class Venta {
	private int id;
	private LocalDate fecha;

	public Venta(int id, LocalDate fecha) {
		this.id = id;
		this.fecha = fecha;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
}