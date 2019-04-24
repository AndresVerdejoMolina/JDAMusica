package com.example.jdamusica;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements CancionesFragment.ReproducirListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_canciones);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        CancionesFragment fragment = CancionesFragment.newInstance();
        cargar_fragment(fragment);
    }

    @Override
    public void pasarCancion(Cancion cancion) {
        ReproducirFragment fragment = ReproducirFragment.newInstance(cancion);
        cargar_fragment(fragment);

    }


    private void cargar_fragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.canciones_container, fragment).commit();
    }

}
