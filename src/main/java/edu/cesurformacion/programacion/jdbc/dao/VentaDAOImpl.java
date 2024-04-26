package edu.cesurformacion.programacion.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.cesurformacion.programacion.jdbc.model.DetalleVenta;
import edu.cesurformacion.programacion.jdbc.model.Venta;
import edu.cesurformacion.programacion.jdbc.utils.DatabaseConnection;

public class VentaDAOImpl implements VentaDAO {

	private Connection conn;

	public VentaDAOImpl() {
		this.conn = DatabaseConnection.getConnection();
	}

	@Override
	public void registrarVenta(Venta venta, List<DetalleVenta> detalles) {
		String sqlVenta = "INSERT INTO Ventas (fecha_venta) VALUES (?) RETURNING venta_id";
		String sqlDetalleVenta = "INSERT INTO DetallesVenta (venta_id, producto_id, cantidad_vendida) VALUES (?, ?, ?)";

		try {
			conn.setAutoCommit(false); // Start transaction

			// Insertar la venta y obtener el ID generado
			try (PreparedStatement pstmtVenta = conn.prepareStatement(sqlVenta)) {
				pstmtVenta.setDate(1, java.sql.Date.valueOf(venta.getFecha()));
				ResultSet rsVenta = pstmtVenta.executeQuery();
				if (rsVenta.next()) {
					int ventaId = rsVenta.getInt(1); // Obtener el ID generado para la venta
					venta.setId(ventaId); // Setear el ID a la venta

					// Insertar cada detalle de la venta
					try (PreparedStatement pstmtDetalle = conn.prepareStatement(sqlDetalleVenta)) {
						for (DetalleVenta detalle : detalles) {
							pstmtDetalle.setInt(1, ventaId);
							pstmtDetalle.setInt(2, detalle.getProductoId());
							pstmtDetalle.setInt(3, detalle.getCantidad());
							pstmtDetalle.executeUpdate();

							// Actualizar stock del producto
							actualizarStockProducto(detalle.getProductoId(), -detalle.getCantidad());
						}
					}
				}
				conn.commit(); // Commit the transaction
			}
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback(); // Roll back the transaction in case of error
				}
			} catch (SQLException ex) {
				System.err.println("Error during transaction rollback: " + ex.getMessage());
			}
			throw new RuntimeException("Error during sale registration: " + e.getMessage(), e);
		} finally {
			try {
				if (conn != null) {
					conn.setAutoCommit(true); // Reset auto-commit to true
				}
			} catch (SQLException e) {
				System.err.println("Error resetting auto-commit: " + e.getMessage());
			}
		}
	}

	private void actualizarStockProducto(int productoId, int cantidadCambiada) throws SQLException {
		String sql = "UPDATE Productos SET cantidad = cantidad + ? WHERE producto_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, cantidadCambiada);
			pstmt.setInt(2, productoId);
			pstmt.executeUpdate();
		}
	}

	@Override
	public void registrarVentaConBatch(Venta venta, List<DetalleVenta> detalles) {
		String sqlVenta = "INSERT INTO Ventas (fecha_venta) VALUES (?) RETURNING venta_id";
		String sqlDetalleVenta = "INSERT INTO DetallesVenta (venta_id, producto_id, cantidad_vendida) VALUES (?, ?, ?)";

		try {
			conn.setAutoCommit(false); // Start transaction
			// Insertar la venta y obtener el ID generado
			try (PreparedStatement pstmtVenta = conn.prepareStatement(sqlVenta)) {
				pstmtVenta.setDate(1, java.sql.Date.valueOf(venta.getFecha()));
				ResultSet rsVenta = pstmtVenta.executeQuery();
				if (rsVenta.next()) {
					int ventaId = rsVenta.getInt(1); // Obtener el ID generado para la venta
					venta.setId(ventaId); // Setear el ID a la venta

					// Insertar cada detalle de la venta utilizando batch
					try (PreparedStatement pstmtDetalle = conn.prepareStatement(sqlDetalleVenta)) {
						for (DetalleVenta detalle : detalles) {
							pstmtDetalle.setInt(1, ventaId);
							pstmtDetalle.setInt(2, detalle.getProductoId());
							pstmtDetalle.setInt(3, detalle.getCantidad());
							pstmtDetalle.addBatch(); // Agregar a batch en lugar de ejecutar inmediatamente
						}
						pstmtDetalle.executeBatch(); // Ejecutar todas las inserciones del batch juntas

						// Actualizar stock del producto en un batch
						try (PreparedStatement pstmtUpdateStock = conn.prepareStatement(
								"UPDATE Productos SET cantidad = cantidad - ? WHERE producto_id = ?")) {
							for (DetalleVenta detalle : detalles) {
								pstmtUpdateStock.setInt(1, detalle.getCantidad());
								pstmtUpdateStock.setInt(2, detalle.getProductoId());
								pstmtUpdateStock.addBatch();
							}
							pstmtUpdateStock.executeBatch(); // Ejecutar todas las actualizaciones del batch juntas
						}
					}
				}
				conn.commit(); // Commit the transaction
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.rollback(); // Roll back the transaction in case of error
					}
				} catch (SQLException ex) {
					System.err.println("Error during transaction rollback: " + ex.getMessage());
				}
				throw new RuntimeException("Error during sale registration: " + e.getMessage(), e);
			} finally {
				try {
					if (conn != null) {
						conn.setAutoCommit(true); // Reset auto-commit to true
					}
				} catch (SQLException e) {
					System.err.println("Error resetting auto-commit: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private PreparedStatement actualizarStockProductoBatch(int productoId, int cantidadCambiada) throws SQLException {
		String sql = "UPDATE Productos SET cantidad = cantidad + ? WHERE producto_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, cantidadCambiada);
			pstmt.setInt(2, productoId);
			pstmt.addBatch();
			return pstmt;
		}
	}

}

/**
 * 
 * ### Explicaciones sobre el Código
 * 
 * 1. **Transacción**: La operación comienza configurando la conexión para no
 * auto-commit, lo que permite controlar cuándo se confirman las operaciones
 * como una unidad completa.
 * 
 * 2. **Registro de Venta**: Se inserta la venta y se recupera el ID de venta
 * generado para usar en los detalles de la venta.
 * 
 * 3. **Registro de Detalles de Venta**: Para cada `DetalleVenta`, se inserta el
 * registro en la base de datos y se actualiza el stock del producto
 * correspondiente.
 * 
 * 4. **Manejo de Excepciones**: Se capturan las excepciones SQL, y en caso de
 * error, se hace rollback de la transacción para mantener la consistencia de la
 * base de datos.
 * 
 * 5. **Cierre de Recursos**: Se asegura que todos los recursos de la base de
 * datos se cierren correctamente y que se restablezca la configuración de
 * auto-commit al finalizar la operación.
 * 
 * Este código maneja correctamente las transacciones de ventas, asegurando que
 * todos los pasos se ejecuten como una operación atómica, /
 */