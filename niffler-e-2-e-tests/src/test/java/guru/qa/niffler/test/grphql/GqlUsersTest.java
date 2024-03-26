package guru.qa.niffler.test.grphql;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GqlRequestFile;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.gql.GqlUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GqlUsersTest extends BaseGraphQLTest {

  @Test
  @ApiLogin(
      user = @TestUser
  )
  void currentUserShouldBeReturned(@User UserJson testUser,
                                   @Token String bearerToken,
                                   @GqlRequestFile("gql/currentUserQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.currentUser(bearerToken, request);
    Assertions.assertEquals(
        testUser.username(),
        response.getData().getUser().getUsername()
    );
  }

  @CsvSource({
          "gql/getFriends2FriedsSubQuery.json, Can`t fetch over 2 friends sub-queries",
          "gql/getFriends2InvitationsSubQuery.json, Can`t fetch over 2 invitations sub-queries"
  })
  @ApiLogin(user = @CreateUser(friends = @Friends(count = 2)))
  @ParameterizedTest
  void friendsInfoShouldNotBeReturned(@GqlRequestFile guru.qa.niffler.model.gql.GqlRequest request, String expectedError,
                                      @Token String bearerToken) throws Exception {
    final GqlUserResponse response = gatewayGqlApiClient.getFriends(bearerToken, request);
    Assertions.assertAll(
            () -> Assertions.assertNotNull(response),
            () -> Assertions.assertEquals(1, response.getErrors().size()),
            () -> Assertions.assertEquals(expectedError, response.getErrors().get(0).message()),
            () -> Assertions.assertEquals("BAD_REQUEST", response.getErrors().get(0).extensions().get("classification"))
    );
  }

  @ApiLogin(user = @CreateUser(friends = @Friends(count = 2)))
  @Test
  void friendsInfoShouldBeReturned(@GqlRequestFile("gql/getFriendsQuery.json") GqlRequest request,
                                   @Token String bearerToken) throws Exception {
    final GqlUserResponse response = gatewayGqlApiClient.getFriends(bearerToken, request);
    Assertions.assertAll(
            () -> Assertions.assertNotNull(response),
            () -> Assertions.assertEquals(2, response.getData().getUser().getFriends().size())
    );
  }

  @ApiLogin(user = @CreateUser(friends = @Friends(
          pending = true,
          friendshipRequestType = FriendshipRequestType.INCOME
  )))
  @Test
  void friendsInvitesInfoShouldBeReturned(@GqlRequestFile("gql/getFriendsQuery.json") GqlRequest request,
                                          @Token String bearerToken) throws Exception {
    final GqlUserResponse response = gatewayGqlApiClient.getFriends(bearerToken, request);
    Assertions.assertAll(
            () -> Assertions.assertNotNull(response),
            () -> Assertions.assertEquals(1, response.getData().getUser().getInvitations().size())
    );
  }

  @Test
  @ApiLogin(user = @CreateUser)
  void userShouldBeUpdated(@Token String bearerToken,
                           @GqlRequestFile("gql/updateUserQuery.json") GqlRequest request) throws Exception {
    final GqlUpdateUser response = gatewayGqlApiClient.updateUser(bearerToken, request);
    Assertions.assertEquals("Pizzly", response.getData().getUpdateUser().getFirstname());
    Assertions.assertEquals("Pizzlyvich", response.getData().getUpdateUser().getSurname());
  }

  @Test
  @ApiLogin(user = @CreateUser)
  void usersShouldBeReturned(@Token String bearerToken, @GqlRequestFile("gql/usersQuery.json") GqlRequest request)
          throws Exception {
    final GqlUsers response = gatewayGqlApiClient.users(bearerToken, request);
    Assertions.assertFalse(response.getData().getUsers().isEmpty());
  }
}

}
