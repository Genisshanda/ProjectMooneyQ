package com.pab.mooneyq.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private final static String BASE_URL = "https://keuanganku-catatan.000webhostapp.com/catatan-keuangan/";
    public static ApiEndpoint endpoint(){

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .client( client )
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create( ApiEndpoint.class );
    }
}
