package guru.qa.niffler.api.spend;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;

public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        spendApi = retrofit.create(SpendApi.class);
    }

    public SpendJson createSpend(@Nonnull SpendJson spend) throws Exception {
        return spendApi.addSpend(spend)
                .execute()
                .body();
    }
}
