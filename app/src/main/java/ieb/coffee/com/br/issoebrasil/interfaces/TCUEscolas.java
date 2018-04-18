package ieb.coffee.com.br.issoebrasil.interfaces;

import java.util.List;
import ieb.coffee.com.br.issoebrasil.model.Escola;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TCUEscolas {

    public static final String BASE_URL= "http://mobile-aceite.tcu.gov.br:80/nossaEscolaRS/";
    @GET("rest/escolas/latitude/{latitude}/longitude/{longitude}/raio/2/")
    Call<List<Escola>> listaEscola(@Path("latitude") double latitude, @Path("longitude") double longitude);

}
