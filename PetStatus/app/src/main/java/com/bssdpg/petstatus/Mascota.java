package com.bssdpg.petstatus;

/**
 * Created by keffe_000 on 17/09/2017.
 */
public class Mascota {
    String nombre;
    String tipo;
    int años;
    String id;
    String sexo;
    float peso;
    String raza;

    public Mascota( String nombre,String  tipo, int años, String id, String sexo, float peso, String raza) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.años = años;
        this.id=id;
        this.sexo=sexo;
        this.peso=peso;
        this.raza=raza;
    }

    public Mascota() {
        this.nombre = "";
        this.tipo="";
        this.años = 0;
        this.id = "";
        this.sexo="";
        this.peso=0.0f;
        this.raza="";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAños() {
        return años;
    }

    public void setAños(int años) {
        this.años = años;
    }

    public String getTipo() {
        return tipo;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
