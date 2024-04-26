package edu.cesurformacion.programacion.jdbc.service;

import java.util.List;

import edu.cesurformacion.programacion.jdbc.dao.ProductoDAO;
import edu.cesurformacion.programacion.jdbc.dao.ProductoDAOImpl;
import edu.cesurformacion.programacion.jdbc.dao.VentaDAO;
import edu.cesurformacion.programacion.jdbc.dao.VentaDAOImpl;
import edu.cesurformacion.programacion.jdbc.model.DetalleVenta;
import edu.cesurformacion.programacion.jdbc.model.Producto;
import edu.cesurformacion.programacion.jdbc.model.Venta;

public class VentaService {
	VentaDAO ventaDAO;
	ProductoDAO productoDAO;

	public VentaService() {
		ventaDAO = new VentaDAOImpl();
		productoDAO = new ProductoDAOImpl();
	}



}