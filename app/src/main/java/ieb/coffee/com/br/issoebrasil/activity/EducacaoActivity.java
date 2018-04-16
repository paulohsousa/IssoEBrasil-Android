package ieb.coffee.com.br.issoebrasil.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ieb.coffee.com.br.issoebrasil.R;
import ieb.coffee.com.br.issoebrasil.interfaces.TCUEscolas;
import ieb.coffee.com.br.issoebrasil.model.Escola;
import ieb.coffee.com.br.issoebrasil.model.Endereco;
import ieb.coffee.com.br.issoebrasil.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class EducacaoActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usuario = new Usuario();

        requestPermission();

        /*
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.map, new MapsActivity(), "MapsFragment");
        transaction.commitAllowingStateLoss();*/


    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void buscarDados() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TCUEscolas.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TCUEscolas service = retrofit.create(TCUEscolas.class);
        Call<List<Escola>> escolas = service.listaEscola(usuario.getLatitude(), usuario.getLongitude());
        escolas.enqueue(new Callback<List<Escola>>() {
            @Override
            public void onResponse(Call<List<Escola>> call, Response<List<Escola>> response) {
                List<Escola> lista = response.body();

                for (Escola r : lista) {
                    Endereco endereco = r.getEndereco();
                    Log.d("RETROFIT", r.getCodEscola() + " " + endereco.getUf());
                }
            }

            @Override
            public void onFailure(Call<List<Escola>> call, Throwable t) {
                Log.e("Erro", "Erro: "+ t.getMessage());
            }
        });


    }
}


