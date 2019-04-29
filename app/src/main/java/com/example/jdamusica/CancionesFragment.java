package com.example.jdamusica;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CancionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancionesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ListView mCancionesList;
    private AdaptadorCanciones mCancionesAdapter;

    private ReproducirListener mListener;
    public ArrayList<Cancion> canciones;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CancionesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CancionesFragment newInstance() {
        CancionesFragment fragment = new CancionesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canciones = new ArrayList<Cancion>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_canciones, container, false);

        mCancionesList = (ListView) view.findViewById(R.id.canciones_list);

        mCancionesAdapter = new AdaptadorCanciones(getActivity(), canciones);

        mCancionesList.setAdapter(mCancionesAdapter);

        mCancionesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cancion currentCancion= mCancionesAdapter.getItem(position);

                Toast.makeText(getActivity(),
                        "Reproduciendo: \n" + currentCancion.getNombreCancion() + " de " + currentCancion.getNombreArtista(),
                        Toast.LENGTH_SHORT).show();

                mListener.pasarCancion(currentCancion);

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ReproducirListener) {
            mListener = (ReproducirListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface ReproducirListener {
        // TODO: Update argument type and name
        void pasarCancion(Cancion cancion);
    }

    public void addCancion(Cancion cancion){
        canciones.add(cancion);
        mCancionesAdapter.notifyDataSetChanged();
    }
}
