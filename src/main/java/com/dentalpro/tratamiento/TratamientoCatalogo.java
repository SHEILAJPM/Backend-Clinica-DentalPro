package com.dentalpro.tratamiento;

import jakarta.persistence.*;

@Entity
@Table(name = "tratamiento_catalogo")
public class TratamientoCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String v) { this.nombre = v; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String v) { this.descripcion = v; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double v) { this.precio = v; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer v) { this.duracionMinutos = v; }
}
