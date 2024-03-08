package guru.qa.niffler.api.userData;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserDataApi {

    @GET("/currentUser")
    Call<UserJson> currentUser(@Query("username") String username);
}
