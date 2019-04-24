package com.example.jdamusica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.bumptech.glide.Glide;

public class AdaptadorCanciones extends ArrayAdapter<Cancion> {

   public AdaptadorCanciones(Context context, List<Cancion> objects){
       super(context, 0, objects);
   }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        // ¿Ya se infló este view?
        if (null == convertView) {
            //Si no existe, entonces inflarlo con itemlist.xml
            convertView = inflater.inflate(
                    R.layout.itemlist,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.fotoCancion = (ImageView) convertView.findViewById(R.id.fotoCancion);
            holder.nombreCancion = (TextView) convertView.findViewById(R.id.nombreCancion);
            holder.nombreArtista = (TextView) convertView.findViewById(R.id.nombreArtista);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Cancion actual.
        Cancion lead = getItem(position);

        // Setup.
        holder.nombreCancion.setText(lead.getNombreCancion());
        holder.nombreArtista.setText(lead.getNombreArtista());
        Glide.with(getContext()).load(lead.getmImage()).into(holder.fotoCancion);

        return convertView;
    }

    static class ViewHolder {
        ImageView fotoCancion;
        TextView nombreCancion;
        TextView nombreArtista;
    }

}
