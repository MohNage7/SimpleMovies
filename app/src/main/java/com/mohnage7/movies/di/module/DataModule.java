package com.mohnage7.movies.di.module;

import android.app.Application;
import android.os.Build;

import com.mohnage7.movies.api.RestApiService;
import com.mohnage7.movies.db.AppExecutors;
import com.mohnage7.movies.db.MovieDatabase;
import com.mohnage7.movies.repository.MovieDetailsRepository;
import com.mohnage7.movies.repository.MoviesRepository;
import com.mohnage7.movies.utils.LiveDataCallAdapterFactory;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mohnage7.movies.utils.Constants.API_KEY;
import static com.mohnage7.movies.utils.Constants.BASE_URL;
import static com.mohnage7.movies.utils.Constants.CONNECTION_TIMEOUT;
import static com.mohnage7.movies.utils.Constants.READ_TIMEOUT;
import static com.mohnage7.movies.utils.Constants.WRITE_TIMEOUT;

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
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                //converts Retrofit response into Observable
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
    }

    /**
     * @return A configured {@link okhttp3.OkHttpClient} that will be used for executing network requests
     */
    @Provides
    @Singleton
    OkHttpClient providesHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // add request time out
        builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
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
    @Singleton
    AppExecutors providesAppExcutor() {
        return AppExecutors.getInstance();
    }

    @Provides
    MovieDetailsRepository providesMovieDetailsRepository(RestApiService apiService, AppExecutors appExecutors) {
        return new MovieDetailsRepository(apiService, appExecutors, application);
    }

    @Provides
    MoviesRepository providesMoviesRepository(RestApiService apiService, MovieDatabase database, AppExecutors appExecutors) {
        return new MoviesRepository(apiService, database, appExecutors);
    }

    @Provides
    @Singleton
    MovieDatabase providesDatabase(){
        return MovieDatabase.getDatabaseInstance(application);
    }


}
