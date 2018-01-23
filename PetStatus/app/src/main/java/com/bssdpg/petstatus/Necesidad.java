package com.bssdpg.petstatus;

/**
 * Created by keffe_000 on 22/09/2017.
 */
public class Necesidad {
    String tipo;
    String frecuencia;
    String detalle;
    String id;
    String idAlarma;

    public Necesidad(String tipo, String frecuencia, String detalle, String id, String idAlarma) {
        this.tipo = tipo;
        this.frecuencia = frecuencia;
        this.detalle = detalle;
        this.id = id;
        this.idAlarma = idAlarma;
    }

    public Necesidad() {
        this.tipo = "";
        this.frecuencia = "";
        this.detalle = "";
        this.id = "";
        this.idAlarma = "";
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAlarma() { return idAlarma; }

    public void setIdAlarma(String idAlarma) { this.idAlarma = idAlarma; }
}
