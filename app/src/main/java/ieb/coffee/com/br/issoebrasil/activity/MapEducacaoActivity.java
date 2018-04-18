package ieb.coffee.com.br.issoebrasil.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ieb.coffee.com.br.issoebrasil.R;
import ieb.coffee.com.br.issoebrasil.interfaces.TCUEscolas;
import ieb.coffee.com.br.issoebrasil.interfaces.TCURemedios;
import ieb.coffee.com.br.issoebrasil.model.Endereco;
import ieb.coffee.com.br.issoebrasil.model.Escola;
import ieb.coffee.com.br.issoebrasil.model.Remedio;
import ieb.coffee.com.br.issoebrasil.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapEducacaoActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    public static final int MAP_PERMISSION_ACCESS_COURSE_LOCATION = 9999;
    LocationManager locationManager;
    private Usuario usuario;
    private List<Escola> listaEscolas;

    private TextView titulo;
    private TextView subtitulo;
    private TextView rede;
    private TextView email;
    private TextView esferaAdministrativa;
    private TextView categoriaEscolaPrivada;
    private TextView situacaoFuncionamento;
    private TextView telefone;
    private TextView seFimLucrativo;
    private TextView qtdSalasExistentes;
    private TextView qtdSalasUtilizadas;
    private TextView qtdFuncionarios;
    private TextView qtdComputadores;
    private TextView qtdComputadoresPorAluno;
    private TextView qtdAlunos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_educacao);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        usuario = new Usuario();

    }

    private void buscarDadosEscolas() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TCUEscolas.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TCUEscolas service = retrofit.create(TCUEscolas.class);

        Call<List<Escola>> escolas = service.listaEscola(usuario.getLatitude(), usuario.getLongitude());


        escolas.enqueue(new Callback<List<Escola>>() {
            @Override
            public void onResponse(Call<List<Escola>> call,
                                   Response<List<Escola>> response) {
                Log.d("Response Body", " "+ response.body());
                listaEscolas = response.body();
                for (Escola r : listaEscolas) {
                    Log.d("RETROFIT", r.getCodEscola() + " " + r.getNome());
                    LatLng latlong = new LatLng(r.getLatitude(), r.getLongitude());
                    Marker mPerth = mMap.addMarker(new MarkerOptions().position(latlong).title(" "+ r.getNome()).snippet(r.getCategoriaEscolaPrivada()));
                    mPerth.setTag(r);
                    mMap.setOnMarkerClickListener(MapEducacaoActivity.this);
                }

                LatLng me = new LatLng(usuario.getLatitude(), usuario.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 15));

            }

            @Override
            public void onFailure(Call<List<Escola>> call,
                                  Throwable t) {
                Log.e("Erro", "Erro: "+ t.getMessage());
            }
        });
    }

    private void buscarDados() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mobile-aceite.tcu.gov.br/mapa-da-saude/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TCURemedios service = retrofit.create(TCURemedios.class);

        Call<List<Remedio>> remedios = service.listarRemedios();

        remedios.enqueue(new Callback<List<Remedio>>() {
            @Override
            public void onResponse(Call<List<Remedio>> call,
                                   Response<List<Remedio>> response) {
                List<Remedio> lista = response.body();

                for (Remedio r : lista) {
                    Log.d("RETROFIT", r.getProduto() + " " + r.getApresentacao());
                }
            }

            @Override
            public void onFailure(Call<List<Remedio>> call,
                                  Throwable t) {
                Log.e("Erro", "Erro: "+ t.getMessage());
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MAP_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {
            getLastLocation();
            getLocation();
            buscarDadosEscolas();
        }

        mMap.setOnInfoWindowClickListener(MapEducacaoActivity.this);


    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(lastKnownLocation == null){
                usuario.setLatitude(-15.7571194);
                usuario.setLongitude(-47.8788442);
            } else {
                usuario.setLatitude(lastKnownLocation.getLatitude());
                usuario.setLongitude(lastKnownLocation.getLongitude());
            }



            /*Log.i("Log", "latitude: "+ usuario.getLatitude()+" longitude: "+ usuario.getLongitude());
            LatLng me = new LatLng(usuario.getLatitude(), usuario.getLongitude());
            mMap.addMarker(new MarkerOptions().position(me).title("Eu estava aqui quando o anrdoid me localizou pela última vez!!!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 12));
            */

        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Escola escola = (Escola) marker.getTag();
        if (escola != null) {
            titulo = (TextView) findViewById(R.id.titulo);
            rede = (TextView) findViewById(R.id.rede);
            email = (TextView) findViewById(R.id.email);
            esferaAdministrativa = (TextView) findViewById(R.id.esferaAdministrativa);
            categoriaEscolaPrivada = (TextView) findViewById(R.id.categoriaEscolaPrivada);
            situacaoFuncionamento = (TextView) findViewById(R.id.situacaoFuncionamento);
            telefone = (TextView) findViewById(R.id.telefone);
            seFimLucrativo = (TextView) findViewById(R.id.seFimLucrativo);
            qtdSalasExistentes = (TextView) findViewById(R.id.qtdSalasExistentes);
            qtdSalasUtilizadas = (TextView) findViewById(R.id.qtdSalasUtilizadas);
            qtdFuncionarios = (TextView) findViewById(R.id.qtdFuncionarios);
            qtdComputadores = (TextView) findViewById(R.id.qtdComputadores);
            qtdComputadoresPorAluno = (TextView) findViewById(R.id.qtdComputadoresPorAluno);
            qtdAlunos = (TextView) findViewById(R.id.qtdAlunos);

            titulo.setText(escola.getNome());
            rede.setText(escola.getRede());
            email.setText(escola.getEmail());
            categoriaEscolaPrivada.setText(escola.getCategoriaEscolaPrivada());
            telefone.setText(escola.getTelefone());
            situacaoFuncionamento.setText(escola.getSituacaoFuncionamento());
            esferaAdministrativa.setText(escola.getEsferaAdministrativa());
            seFimLucrativo.setText(escola.getSeFimLucrativo());
            qtdSalasExistentes.setText(escola.getQtdSalasExistentes().toString());
            qtdSalasUtilizadas.setText(escola.getQtdSalasUtilizadas().toString());
            qtdFuncionarios.setText(escola.getQtdFuncionarios().toString());
            qtdComputadores.setText(escola.getQtdComputadores().toString());
            qtdComputadoresPorAluno.setText(escola.getQtdComputadoresPorAluno().toString());
            qtdAlunos.setText(escola.getQtdAlunos().toString());


            /*Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + escola.getNome() + " times.",
                    Toast.LENGTH_SHORT).show();*/
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    if(location == null){
                        usuario.setLatitude(-15.7571194);
                        usuario.setLongitude(-47.8788442);
                    }else{
                        usuario.setLatitude(location.getLatitude());
                        usuario.setLongitude(location.getLongitude());
                    }

                    /*Log.i("Log", "latitude: "+ usuario.getLatitude()+" longitude: "+ usuario.getLongitude());
                    LatLng me = new LatLng(usuario.getLatitude(), usuario.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(me).title("Estou Aqui!!!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 20));*/
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MAP_PERMISSION_ACCESS_COURSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                    getLocation();
                } else {
                    //Permissão negada

                }
                return;
            }
        }
    }
}
