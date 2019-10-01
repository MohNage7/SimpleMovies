package com.mohnage7.movies.di.module;

import android.app.Application;
import android.os.Build;

import com.mohnage7.movies.repository.MoviesRepository;
import com.mohnage7.movies.service.RestApiService;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mohnage7.movies.utils.Constants.API_KEY;
import static com.mohnage7.movies.utils.Constants.BASE_URL;

@Module
public class DataModule {
    private Application application;

    public DataModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                //converts Retrofit response into Observable
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * @return A configured {@link okhttp3.OkHttpClient} that will be used for executing network requests
     */
    @Provides
    @Singleton
    OkHttpClient providesHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /*
           The following code will trust all certificate in the hand shake protocol between
           The device's OS and the server and it's not recommended in production environment
           I will use it only to test on kitkat and change it later if i had time.
           // TODO: 9/29/2019 Replace with allowing only specific server certificate https://developer.android.com/training/articles/security-ssl.html#CommonProblems
         */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                builder.sslSocketFactory(sc.getSocketFactory());
                builder.hostnameVerifier((arg0, arg1) -> true);
            } catch (Exception ignored) {
            }
        }
        // add request time out
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(40, TimeUnit.SECONDS);
        builder.writeTimeout(40, TimeUnit.SECONDS);
        // Add logging into
        builder.interceptors().add(httpLoggingInterceptor);
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            requestBuilder.addHeader("Authorization",API_KEY);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        return builder.build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor providesHttpLogging(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Provides
    @Singleton
    RestApiService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestApiService.class);
    }

    @Provides
    MoviesRepository providesRepository(RestApiService apiService) {
        return new MoviesRepository(apiService);
    }
}
