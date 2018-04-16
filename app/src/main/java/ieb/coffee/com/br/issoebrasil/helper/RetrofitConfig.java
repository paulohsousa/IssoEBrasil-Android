package ieb.coffee.com.br.issoebrasil.helper;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;

    public RetrofitConfig(Retrofit retrofit) {
        this.retrofit = retrofit;
        new Retrofit.Builder().baseUrl("http://mobile-aceite.tcu.gov.br/nossaEscolaRS/rest/escolas")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

}
