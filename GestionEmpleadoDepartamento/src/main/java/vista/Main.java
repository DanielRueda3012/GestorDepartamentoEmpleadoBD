package vista;

import java.sql.SQLException;

import gestor.Operaciones;
import io.IO;
import model.Departamento;
import model.Empleado;

public class Main {

    public static void main(String[] args) throws SQLException {
        Operaciones operacion = new Operaciones();

        while (true) {
           IO.println("\n*** Menú ***");
           IO.println("1. Agregar Departamento");
           IO.println("2. Agregar Empleado");
           IO.println("3. Asignar Jefe a Departamento");
           IO.println("4. Asignar Departamento a Empleado"); 
           IO.println("5. Eliminar Departamento");
           IO.println("6. Mostrar Tabla Departamento");
           IO.println("7. Mostrar Tabla Empleado");
           IO.println("8. Salir");
           IO.print("Elija una opción: ");

            int opcion =IO.readInt();

            switch (opcion) {
                case 1:
                	IO.print("Ingrese el nombre del departamento: ");
                    String nombreDepartamento = IO.readString();
                    Departamento departamento = new Departamento(nombreDepartamento, null);
				operacion.agregarDepartamento(departamento);
                    break;
                case 2:
                	IO.print("Ingrese el nombre del empleado: ");
                    String nombreEmpleado = IO.readString();
                    IO.print("Ingrese el salario del empleado: ");
                    Double salarioEmpleado =IO.readDouble();
                    IO.println("SE VA A ASIGNAR DEPARTAMENTO NULL POR DEFECTO, GRACIAS");
                    
                    Departamento departamentoEmpleado = null; // El departamento es null por defecto

                    Empleado empleado = new Empleado(nombreEmpleado, salarioEmpleado, departamentoEmpleado);
                    operacion.agregarEmpleado(empleado);
                    break;

					
                case 3:
				operacion.asignarJefe();
                    break;
                case 4:
				try {
					operacion.asignarEmpleadoADepartamento();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
                    break;
                case 5:
				operacion.eliminarDepartamento();
                    break;
                case 6:
				operacion.mostrarTablaDepartamento();;
                    break;
                case 7:
				operacion.mostrarTablaEmpleado();;
                    break;
                case 8:
                    operacion.close();
                    return;
                    
                    // Salir del programa
                
                
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción del menú.");
            }
        }
    }
}

