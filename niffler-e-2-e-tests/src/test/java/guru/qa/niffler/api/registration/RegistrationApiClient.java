package guru.qa.niffler.api.registration;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.api.cookie.ThreadSafeCookieManager;
import guru.qa.niffler.api.interceptor.CodeInterceptor;
import lombok.SneakyThrows;

public class RegistrationApiClient extends RestClient {

    private final RegistrationApi registrationApi;

    public RegistrationApiClient() {
        super(CFG.authUrl(),
                false,
                new CodeInterceptor());
        registrationApi = retrofit.create(RegistrationApi.class);
    }

    @SneakyThrows
    public void doRegistrationUser(String username, String password){
        registrationApi.register().execute();
        final String XSRF_TOKEN = ThreadSafeCookieManager.INSTANCE.getCookieValue("XSRF-TOKEN");
        registrationApi.registerPost(
                "XSRF-TOKEN=" + XSRF_TOKEN,
                username,
                password,
                password,
                XSRF_TOKEN
        ).execute();
    }
}
