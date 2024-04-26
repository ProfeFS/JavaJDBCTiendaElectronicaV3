package edu.cesurformacion.programacion.jdbc.service;

import java.util.List;

import edu.cesurformacion.programacion.jdbc.dao.ProductoDAO;
import edu.cesurformacion.programacion.jdbc.dao.ProductoDAOImpl;
import edu.cesurformacion.programacion.jdbc.model.Producto;

public class ProductoService {
    private ProductoDAO productoDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDAOImpl();
    }

    public Producto obtenerProductoPorId(int id) {
        return productoDAO.obtenerProductoPorId(id);
    }

    public List<Producto> listarProductos() {
        return productoDAO.listarProductos();
    }

    public void agregarProducto(Producto producto) {
        if (producto != null && producto.getCantidad() >= 0) {
            productoDAO.agregarProducto(producto);
        } else {
            throw new IllegalArgumentException("Información de producto inválida.");
        }
    }

    public void actualizarProducto(Producto producto) {
        if (producto != null && producto.getCantidad() >= 0) {
            productoDAO.actualizarProducto(producto);
        } else {
            throw new IllegalArgumentException("Información de producto inválida para actualizar.");
        }
    }
}