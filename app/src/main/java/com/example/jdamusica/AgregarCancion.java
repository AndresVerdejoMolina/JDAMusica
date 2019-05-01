package com.example.jdamusica;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NuevaCancion} interface
 * to handle interaction events.
 * Use the {@link AgregarCancion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarCancion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText nombreCancion, nombreArtista;
    TextView addPhotoText, addMusicText;
    ImageButton addMusica, addPhoto;

    private Uri uri;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NuevaCancion mListener;

    public AgregarCancion() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static AgregarCancion newInstance() {
        AgregarCancion fragment = new AgregarCancion();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agregar_cancion, container, false);
        nombreArtista = view.findViewById(R.id.nombreArtistaAdd);
        nombreCancion = view.findViewById(R.id.nombreCancionAdd);

        Button subirFoto = view.findViewById(R.id.subirFoto);

        addPhotoText = view.findViewById(R.id.addPhotoText);
        addMusicText = view.findViewById(R.id.addMusicText);

        addMusica = view.findViewById(R.id.addMusic);
        addPhoto = view.findViewById(R.id.addPhoto);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagenGaleria(nombreArtista.getText().toString().trim(), nombreCancion.getText().toString().trim());
            }
        });

        addMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarAudio(nombreArtista.getText().toString().trim(), nombreCancion.getText().toString().trim());
            }
        });

        subirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirFotoStorage(nombreArtista.getText().toString().trim(), nombreCancion.getText().toString().trim());
            }
        });

        return view;
    }

    private void subirFotoStorage(final String nombreArtista, final String nombreCancion) {
        if(uri == null) {
            Toast.makeText(getActivity(),
                    "Primero añade un archivo",
                    Toast.LENGTH_SHORT).show();
            return;
        }else{
            mListener.agregarNuevaCancion(nombreCancion, nombreArtista, uri);
        }
    }

    private void cargarAudio(String nombreArtista, String nombreCancion) {
        if(nombreArtista.isEmpty() || nombreCancion.isEmpty()){
            Toast.makeText(getActivity(),
                    "Primero rellena los campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 10);
    }

    private void cargarImagenGaleria(String nombreArtista, String nombreCancion) {
        if(nombreArtista.isEmpty() || nombreCancion.isEmpty()){
            Toast.makeText(getActivity(),
                    "Primero rellena los campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //Abro la petición de Intent pero esperando una respuesta
        //porque en seleccionar un imagen quiero volver a mi App y
        //quiero conocer que imagen se ha seleccionado, esta info la
        //recibimos en el metodo onActivityResult
        startActivityForResult(intent, 10);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NuevaCancion) {
            mListener = (NuevaCancion) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NuevaCancion");
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
    public interface NuevaCancion {
        // TODO: Update argument type and name
        void agregarNuevaCancion(String nombreCancion, String nombreArtista, Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;

        if (requestCode == 10 && resultCode == RESULT_OK) {


            uri = data.getData();

            try {

                //Puedo conseguir un bitmap directamente desde una Uri
                //Para ello utilizo la clase MediaStore.Images
                bitmap = MediaStore.Images.Media
                        .getBitmap(getContext().getContentResolver(), uri);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
