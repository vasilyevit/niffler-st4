package guru.qa.niffler.api.userData;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UserDataApiClient extends RestClient {

    private final UserDataApi userDataApi;

    public UserDataApiClient() {
        super(CFG.userdataUrl());
        userDataApi = retrofit.create(UserDataApi.class);
    }

    @Nullable
    public UserJson getCurrentUser(@Nonnull String username) throws Exception {
        return userDataApi.currentUser(username)
                .execute()
                .body();
    }

}
