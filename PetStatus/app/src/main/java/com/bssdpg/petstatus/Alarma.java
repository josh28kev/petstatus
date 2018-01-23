package com.bssdpg.petstatus;

import android.app.PendingIntent;
import android.content.Intent;

public class Alarma {

    private String dueño;
    private String mascota;
    private int identificador;
    private int numeroElemento;
    private PendingIntent intent;

    public Alarma(String dueño, String mascota, int identificador, PendingIntent intent, int numeroElemento) {
        this.dueño = dueño;
        this.mascota = mascota;
        this.identificador = identificador;
        this.intent = intent;
        this.numeroElemento = numeroElemento;
    }

    public PendingIntent getIntent() {
        return intent;
    }

    public void setIntent(PendingIntent intent) {
        this.intent = intent;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public String getDueño() {
        return dueño;
    }

    public void setDueño(String dueño) {
        this.dueño = dueño;
    }

    public int getNumeroElemento() {
        return numeroElemento;
    }

    public void setNumeroElemento(int numeroElemento) {
        this.numeroElemento = numeroElemento;
    }
}
