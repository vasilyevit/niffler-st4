package guru.qa.niffler.api.category;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.model.CategoryJson;

import javax.annotation.Nonnull;

public class CategoryApiClient extends RestClient {

    private final CategoryApi categoryApi;

    public CategoryApiClient() {
        super(CFG.spendUrl());
        categoryApi = retrofit.create(CategoryApi.class);
    }

    public CategoryJson createCategory(@Nonnull CategoryJson category) throws Exception {
        return categoryApi.addCategory(category)
                .execute()
                .body();
    }
}
