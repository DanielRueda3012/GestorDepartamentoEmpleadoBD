package model;

public class Departamento {
    private Integer id;
    private String nombre;
    private Empleado jefe;

    public Departamento( String nombre, Empleado jefe) {
       
        this.nombre = nombre;
        this.jefe = jefe;
    }

    public Departamento() {
	}

	public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Empleado getJefe() {
        return jefe;
    }

    public void setJefe(Empleado jefe) {
        this.jefe = jefe;
    }

}