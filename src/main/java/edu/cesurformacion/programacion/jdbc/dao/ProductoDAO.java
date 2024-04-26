package edu.cesurformacion.programacion.jdbc.dao;

import java.util.List;

import edu.cesurformacion.programacion.jdbc.model.Producto;

public interface ProductoDAO {
	Producto obtenerProductoPorId(int id);

	List<Producto> listarProductos();

	void actualizarProducto(Producto producto);

	void agregarProducto(Producto producto);
}