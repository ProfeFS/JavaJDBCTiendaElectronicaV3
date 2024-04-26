package edu.cesurformacion.programacion.jdbc.dao;

import java.util.List;

import edu.cesurformacion.programacion.jdbc.model.DetalleVenta;
import edu.cesurformacion.programacion.jdbc.model.Venta;

public interface VentaDAO {
	void registrarVenta(Venta venta, List<DetalleVenta> detalles);
	void registrarVentaConBatch(Venta venta, List<DetalleVenta> detalles);
}