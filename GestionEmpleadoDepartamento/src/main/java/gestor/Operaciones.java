package gestor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.InputMismatchException;

import dao.BD;
import io.IO;
import model.Departamento;
import model.Empleado;

public class Operaciones {

	static final String JDBC_URL = "jdbc:sqlite:empresa.db";

	static Connection conn = null;
	static PreparedStatement pstmt = null;

	/**
	 * Constructor
	 */
	public Operaciones() {
		conn = BD.getConnection();
		inicializarBaseDatos();
	}

	/**
	 * Cierra la agenda
	 */
	public void close() {
		BD.close();
	}

	public static void inicializarBaseDatos() {
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet departamentoTable = metaData.getTables(null, null, "departamento", null);
			ResultSet empleadoTable = metaData.getTables(null, null, "empleado", null);

			if (!departamentoTable.next() || !empleadoTable.next()) {
				String sql;

				if (BD.typeDB.equals("sqlite")) {
					sql = """
							    CREATE TABLE IF NOT EXISTS departamento (
							        id INTEGER PRIMARY KEY AUTOINCREMENT,
							        nombre TEXT NOT NULL,
							        jefe INTEGER
							    );

							    CREATE TABLE IF NOT EXISTS empleado (
							        id INT PRIMARY KEY AUTOINCREMENT,
							        nombre VARCHAR(255) NOT NULL,
							        salario REAL DEFAULT 0.0,
							        departamento INT
							    );
							""";
				} else if (BD.typeDB.equals("mariadb")) {
					sql = """
							    CREATE TABLE IF NOT EXISTS departamento (
							        id INT PRIMARY KEY AUTOINCREMENT,
							        nombre VARCHAR(255) NOT NULL,
							        jefe INT
							    );

							    CREATE TABLE IF NOT EXISTS empleado (
							        id INTEGER PRIMARY KEY AUTOINCREMENT,
							        nombre TEXT NOT NULL,
							        salario DECIMAL(10,2) DEFAULT 0.0,
							        departamento INTEGER
							    );
							""";
				} else {
					throw new RuntimeException("Tipo de base de datos no soportado: " + BD.typeDB);
				}

				ejecutarSQL(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void eliminarDepartamento() {
		try {
			System.out.print("Ingrese el ID del departamento a eliminar: ");
			int departamentoId = IO.readInt();
			;

			String updateEmpleados = "UPDATE empleado SET departamento = NULL WHERE departamento = ?";
			pstmt = conn.prepareStatement(updateEmpleados);
			pstmt.setInt(1, departamentoId);
			ejecutarSQL(pstmt);

			String deleteDepartamento = "DELETE FROM departamento WHERE id = ?";
			pstmt = conn.prepareStatement(deleteDepartamento);
			pstmt.setInt(1, departamentoId);
			ejecutarSQL(pstmt);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InputMismatchException e) {
			System.out.println("Error: Ingrese un valor numérico válido.");

		}
	}

	public void agregarDepartamento(Departamento departamento) {
		try {
			String insertDepartamento = "INSERT INTO departamento (nombre, jefe) VALUES (?, ?)";
			pstmt = conn.prepareStatement(insertDepartamento);

			pstmt.setString(1, departamento.getNombre());
			if (departamento.getJefe() != null) {
				pstmt.setInt(2, departamento.getJefe().getId());
			} else {
				pstmt.setNull(2, Types.INTEGER);
			}
			ejecutarSQL(pstmt);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void agregarEmpleado(Empleado empleado) {
		try {
			String insertEmpleado = "INSERT INTO empleado (nombre, salario, departamento) VALUES (?, ?, ?)";
			pstmt = conn.prepareStatement(insertEmpleado);

			pstmt.setString(1, empleado.getNombre());
			pstmt.setDouble(2, empleado.getSalario());

			// Si el departamento es null, simplemente setea el valor como null
			if (empleado.getDepartamento() == null) {
				pstmt.setNull(3, Types.INTEGER);
			} else {
				pstmt.setInt(3, empleado.getDepartamento().getId());
			}

			ejecutarSQL(pstmt);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void asignarJefe() {
		try {
			System.out.print("Ingrese el ID del departamento: ");
			int departamentoId;

			while (true) {
				try {
					departamentoId = Integer.parseInt(IO.readString());
					break;
				} catch (NumberFormatException e) {
					IO.println("Error: Ingrese un valor numérico válido.");
					IO.print("Ingrese el ID del departamento: ");
				}
			}

			System.out.print("Ingrese el ID del empleado que será el jefe: ");
			int jefeId;

			while (true) {
				try {
					jefeId = IO.readInt();
					break;
				} catch (NumberFormatException e) {
					IO.println("Error: Ingrese un valor numérico válido.");
					IO.print("Ingrese el ID del empleado que será el jefe: ");
				}
			}

			String updateDepartamento = "UPDATE departamento SET jefe = ? WHERE id = ?";
			pstmt = conn.prepareStatement(updateDepartamento);
			pstmt.setInt(1, jefeId);
			pstmt.setInt(2, departamentoId);
			ejecutarSQL(pstmt);

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public void mostrarTablaDepartamento() {
		try {
			String sql = "SELECT * FROM departamento";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				int jefe = rs.getInt("jefe");

				IO.println("ID: " + id + ", Nombre: " + nombre + ", ID DEL Jefe: " + jefe);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void mostrarTablaEmpleado() {
		try {
			String sql = "SELECT * FROM empleado";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				double salario = rs.getDouble("salario");
				int departamento = rs.getInt("departamento");

				System.out.println("ID: " + id + ", Nombre: " + nombre + ", Salario: " + salario + ", Departamento: "
						+ departamento);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void asignarEmpleadoADepartamento() throws SQLException {
		int empleadoId, departamentoId;
		System.out.println("ID del empleado");
		empleadoId = IO.readInt();
		System.out.println("ID del departamento");
		departamentoId = IO.readInt();
		String selectEmpleado = "SELECT id FROM empleado WHERE id = ?";
		pstmt = conn.prepareStatement(selectEmpleado);
		pstmt.setInt(1, empleadoId);
		ResultSet empleadoResult = pstmt.executeQuery();

		String selectDepartamento = "SELECT id FROM departamento WHERE id = ?";
		pstmt = conn.prepareStatement(selectDepartamento);
		pstmt.setInt(1, departamentoId);
		ResultSet departamentoResult = pstmt.executeQuery();

		if (empleadoResult.next() && departamentoResult.next()) {
			String updateEmpleado = "UPDATE empleado SET departamento = ? WHERE id = ?";
			pstmt = conn.prepareStatement(updateEmpleado);
			pstmt.setInt(1, departamentoId);
			pstmt.setInt(2, empleadoId);
			ejecutarSQL(pstmt);
			System.out.println("Empleado asignado al departamento correctamente.");
		}

		else {
			System.out.println("El empleado o el departamento especificados no existen.");
		}
	}

	static void ejecutarSQL(PreparedStatement pstmt) throws SQLException {
		pstmt.executeUpdate();
		pstmt.close();
	}

	static void ejecutarSQL(String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}
}