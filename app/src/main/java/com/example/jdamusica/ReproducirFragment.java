package com.example.jdamusica;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.widget.Toast;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReproducirFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReproducirFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReproducirFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private String cancionNombre,artistaNombre, urlFoto, urlCancion;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;

    TextView nombreArtista, nombreCancion;
    ImageView foto;

    ImageButton pausar, play;

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
        bundle.putString("urlFoto", cancion.getFoto());
        bundle.putString("urlCancion", cancion.getUrlCancion());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cancionNombre = getArguments().getString("nombreCancion", "No hay nombre de cancion");
            artistaNombre = getArguments().getString("nombreArtista", "No hay nombre de artista");
            urlFoto = getArguments().getString("urlFoto");
            urlCancion = getArguments().getString("urlCancion");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reproducir, container, false);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(getContext());

        nombreCancion = view.findViewById(R.id.nombreCancionReproducir);
        nombreArtista = view.findViewById(R.id.nombreArtistaReproducir);
        foto = view.findViewById(R.id.fotoCancionReproducir);
        pausar = (ImageButton) view.findViewById(R.id.pauseMusica);
        play = (ImageButton) view.findViewById(R.id.playMusic);
        MiHilo thread = new MiHilo();


        Log.i("Cancion", cancionNombre + '-' + artistaNombre);

        nombreArtista.setText(artistaNombre);
        nombreCancion.setText(cancionNombre);
        thread.execute(urlFoto);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoomin);

                foto.startAnimation(animation);

                Toast.makeText(getActivity(),
                        "Se está ecuchando " + cancionNombre + " de " + artistaNombre,
                        Toast.LENGTH_SHORT).show();
                if(initialStage){
                    new Player().execute(urlCancion);
                }else{
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                    }
                }

            }
        });

        pausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout);

                foto.startAnimation(animation);
                mediaPlayer.pause();
            }
        });

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class MiHilo extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {

            URL url;
            HttpURLConnection connection;
            Bitmap bitmap = null;

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();

                bitmap = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            foto.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mediaPlayer != null)
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
    }

    class Player extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {

            Boolean prepared = false;
             try {

                 mediaPlayer.setDataSource(strings[0]);
                 mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                     @Override
                     public void onCompletion(MediaPlayer mp) {
                         initialStage = true;
                         mediaPlayer.stop();
                         mediaPlayer.reset();
                     }
                 });

                 mediaPlayer.prepare();
                 prepared = true;

             }catch (Exception e){
                 Log.e("AudioStreaming", e.getMessage());
                 prepared = false;
             }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(progressDialog.isShowing()){
                progressDialog.cancel();
            }

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Cargando cancion...");
            progressDialog.show();

        }
    }

}
