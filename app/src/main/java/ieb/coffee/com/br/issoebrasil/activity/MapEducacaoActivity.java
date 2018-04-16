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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ieb.coffee.com.br.issoebrasil.R;
import ieb.coffee.com.br.issoebrasil.interfaces.TCUEscolas;
import ieb.coffee.com.br.issoebrasil.model.Endereco;
import ieb.coffee.com.br.issoebrasil.model.Escola;
import ieb.coffee.com.br.issoebrasil.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapEducacaoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final int MAP_PERMISSION_ACCESS_COURSE_LOCATION = 9999;
    LocationManager locationManager;
    private Usuario usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_educacao);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        usuario = new Usuario();
        getLastLocation();
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
            //getLastLocation();
            //getLocation();
        }

        LatLng iesbSul = new LatLng(-15.7571194, -47.8788442);
        mMap.addMarker(new MarkerOptions().position(iesbSul).title("IESB Sul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(iesbSul, 9));
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            usuario.setLatitude(lastKnownLocation.getLatitude());
            usuario.setLongitude(lastKnownLocation.getLongitude());
            Log.i("Log", "latitude: "+ usuario.getLatitude()+" longitude: "+ usuario.getLongitude());
            LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(me).title("Eu estava aqui quando o anrdoid me localizou pela última vez!!!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 12));
        }
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    usuario.setLatitude(location.getLatitude());
                    usuario.setLongitude(location.getLongitude());
                    Log.i("Log", "latitude: "+ usuario.getLatitude()+" longitude: "+ usuario.getLongitude());
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(me).title("Estou Aqui!!!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 20));
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
