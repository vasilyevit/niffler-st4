package guru.qa.niffler.api.category;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface CategoryApi {

    @POST("/category")
    Call<CategoryJson> addCategory(@Body CategoryJson categoryJson);
}
