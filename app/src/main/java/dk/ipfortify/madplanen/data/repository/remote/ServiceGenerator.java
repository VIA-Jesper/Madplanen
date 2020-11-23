package dk.ipfortify.madplanen.data.repository.remote;

import dk.ipfortify.madplanen.data.repository.remote.api.nemlig.NemligApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final Retrofit.Builder getRetrofitBuilderNemlig = new Retrofit.Builder()
            .baseUrl("https://www.nemlig.com/")
            .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofitNemlig = getRetrofitBuilderNemlig.build();
    private static final NemligApi apiService = retrofitNemlig.create(NemligApi.class);

    public static NemligApi getApi() {
        return apiService;
    }
}
