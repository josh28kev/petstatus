package com.bssdpg.petstatus;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by keffe_000 on 19/09/2017.
 */
public class ListaMascotasAdapter extends BaseAdapter {

    private final Activity context;
    private final ArrayList<Mascota> mascotas;

    public ListaMascotasAdapter( Activity contexto ,  ArrayList<Mascota> mascotas){
       this.context = contexto;
        this.mascotas =mascotas;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mascotas_layout, null, true);
        TextView nombre = (TextView) rowView.findViewById(R.id.txt_nombreMascotaLV);
        //TextView edad = (TextView) rowView.findViewById(R.id.txt_edadLVE);
        //TextView tipo = (TextView) rowView.findViewById(R.id.txt_tipoLVE);

        nombre.setText(mascotas.get(position).getNombre());
        //edad.setText(mascotas.get(position).getAños()+"");
        //tipo.setText(mascotas.get(position).getTipo());
        return null;
    }
}
