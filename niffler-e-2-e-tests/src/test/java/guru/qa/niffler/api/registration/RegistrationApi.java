package guru.qa.niffler.api.registration;

import retrofit2.Call;
import retrofit2.http.*;

public interface RegistrationApi {

    @GET("/register")
    Call<Void> register();

    @POST("/register")
    @FormUrlEncoded
    Call<Void> registerPost(
            @Header("Cookie") String cookie,
            @Field("username") String username,
            @Field("password") String password,
            @Field("passwordSubmit") String passwordSubmit,
            @Field("_csrf") String csrf
    );
}
