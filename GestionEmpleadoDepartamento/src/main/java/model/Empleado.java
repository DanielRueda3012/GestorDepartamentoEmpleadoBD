package model;

public class Empleado {
    private Integer id;
    private String nombre;
    private Double salario;
    private Departamento departamento;

    public Empleado( String nombre, Double salario, Departamento departamento) {
       
        this.nombre = nombre;
        this.salario = salario;
        this.departamento = departamento;
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

    public double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}