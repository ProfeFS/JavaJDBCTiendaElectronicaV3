package edu.cesurformacion.programacion.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cesurformacion.programacion.jdbc.model.Producto;
import edu.cesurformacion.programacion.jdbc.utils.DatabaseConnection;

public class ProductoDAOImpl implements ProductoDAO {
    private Connection conn;

    public ProductoDAOImpl() {
    	 this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public Producto obtenerProductoPorId(int id) {
        String sql = "SELECT * FROM Productos WHERE producto_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                        rs.getInt("producto_id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad")
                    );
                }
            }
        } catch (SQLException e) {
        	 System.out.println("Error al obtener el producto por ID" + e.getMessage());
        	 e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Producto> listarProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Productos";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                productos.add(new Producto(
                    rs.getInt("producto_id"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad")
                ));
            }
        } catch (SQLException e) {
           System.out.println("Error al listar productos" + e.getMessage());
           e.printStackTrace();
        }
        return productos;
    }

    @Override
    public void actualizarProducto(Producto producto) {
        String sql = "UPDATE Productos SET nombre = ?, categoria = ?, precio = ?, cantidad = ? WHERE producto_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getCategoria());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getCantidad());
            pstmt.setInt(5, producto.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	 System.out.println("Error al actualizar el producto" + e.getMessage());
        	 e.printStackTrace();
        }
    }

    @Override
    public void agregarProducto(Producto producto) {
        String sql = "INSERT INTO Productos (nombre, categoria, precio, cantidad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getCategoria());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getCantidad());
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	 System.out.println("Error al agregar un nuevo producto" + e.getMessage());
        	 e.printStackTrace();
        }
    }
}