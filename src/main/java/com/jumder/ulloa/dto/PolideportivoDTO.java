package com.jumder.ulloa.dto;

import lombok.Data;

@Data
public class PolideportivoDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String ciudad;
    private Boolean activo;
}