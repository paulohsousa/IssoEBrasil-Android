package ieb.coffee.com.br.issoebrasil.interfaces;

import java.util.List;

import ieb.coffee.com.br.issoebrasil.model.Remedio;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TCURemedios {
    @GET("rest/remedios")
    Call<List<Remedio>> listarRemedios();
}
