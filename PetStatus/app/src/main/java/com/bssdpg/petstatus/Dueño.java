package com.bssdpg.petstatus;

public class Dueño {
    String nombre;
    String email;
    String id;
    String direccion;
    int telefono;
    String sexo;

    public Dueño(String nombre, String email, String id, String direccion, int telefono, String sexo) {
        this.nombre = nombre;
        this.email = email;
        this.id = id;
        this.direccion=direccion;
        this.telefono=telefono;
        this.sexo="";
    }

    public Dueño() {
        this.nombre = "";
        this.email = "";
        this.id = "";
        this.direccion="";
        this.telefono=0;
        this.sexo="";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
