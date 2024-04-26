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

	public void registrarVenta(Venta venta, List<DetalleVenta> detalles) {
		if (venta == null || detalles == null || detalles.isEmpty()) {
			System.err.println("Error: La información de la venta o los detalles son inválidos.");
			return;
		}

		// Validar que todos los productos en la venta estén en stock suficiente
		if (!validarStock(detalles)) {
			System.err.println("Error: Stock insuficiente para uno o más productos en la venta.");
			return;
		}

		// Proceder a registrar la venta
		try {
			ventaDAO.registrarVenta(venta, detalles);
			System.out.println("Venta registrada exitosamente.");
		} catch (Exception e) {
			System.err.println("Error al registrar la venta: " + e.getMessage());
			// Log the exception details here for debugging if necessary
		}
	}

	private boolean validarStock(List<DetalleVenta> detalles) {
		try {
			for (DetalleVenta detalle : detalles) {
				Producto producto = productoDAO.obtenerProductoPorId(detalle.getProductoId());
				if (producto == null || producto.getCantidad() < detalle.getCantidad()) {
					return false; // Stock insuficiente
				}
			}
		} catch (Exception e) {
			System.err.println("Error al validar el stock: " + e.getMessage());
			return false; // Error al validar el stock, asumir stock insuficiente
		}
		return true; // Suficiente stock disponible

	}

}