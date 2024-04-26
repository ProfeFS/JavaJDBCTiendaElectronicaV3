package edu.cesurformacion.programacion.jdbc.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cesurformacion.programacion.jdbc.model.DetalleVenta;
import edu.cesurformacion.programacion.jdbc.model.Producto;
import edu.cesurformacion.programacion.jdbc.model.Venta;
import edu.cesurformacion.programacion.jdbc.service.ProductoService;
import edu.cesurformacion.programacion.jdbc.service.VentaService;

public class Main {

	public static void main(String[] args) {

		// Crear instancias de servicios
		ProductoService productoService = new ProductoService();
		VentaService ventaService = new VentaService();

		// Mostrar productos disponibles utilizando el servicio
		List<Producto> productos = productoService.listarProductos();
		System.out.println("Productos disponibles:");
		for (Producto producto : productos) {
			System.out.println(producto);
		}
		
		//realizar una venta
		Venta venta = new Venta(0, LocalDate.now());
		DetalleVenta detalleP1 = new DetalleVenta(0, 0, 1, 3);
		DetalleVenta detalleP2 = new DetalleVenta(0, 0, 2, 6);
		DetalleVenta detalleP3 = new DetalleVenta(0, 0, 3, 1);
		DetalleVenta detalleP4 = new DetalleVenta(0, 0, 4, 2);
		List<DetalleVenta> detalles = Arrays.asList(detalleP1, detalleP2, detalleP3, detalleP4);
		
		ventaService.registrarVenta(venta, detalles);
		
		productos = productoService.listarProductos();
		System.out.println("Productos disponibles:");
		for (Producto producto : productos) {
			System.out.println(producto);
		}

	}

}
