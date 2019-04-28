package com.example.jdamusica;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReproducirFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReproducirFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReproducirFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;
    private Cancion cancion;

    TextView nombreArtista, nombreCancion;
    ImageView foto;

    private OnFragmentInteractionListener mListener;

    public ReproducirFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReproducirFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReproducirFragment newInstance(Cancion cancion) {
        ReproducirFragment fragment = new ReproducirFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nombreCancion", cancion.getNombreCancion());
        bundle.putString("nombreArtista", cancion.getNombreArtista());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("nombreCancion", "No hay nombre de cancion.");
            mParam2 = getArguments().getString("nombreArtista", "No hay nombre del artusta.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reproducir, container, false);

        nombreCancion = view.findViewById(R.id.nombreCancionReproducir);
        nombreArtista = view.findViewById(R.id.nombreArtistaReproducir);
        foto = view.findViewById(R.id.fotoCancionReproducir);

        nombreArtista.setText(mParam2);
        nombreCancion.setText(mParam1);
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
