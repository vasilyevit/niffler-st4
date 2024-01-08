package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.*;

@ExtendWith(UsersQueueExtension.class)
public class FriendsWithOutForEachTest extends BaseWebTest  {

  @Test
  void friendsTableShouldNotBeEmpty(@User(WITH_FRIENDS) UserJson user) {
    login(user.username(),user.testData().password());
    menuPage.goToTabFriends();
    friendsPage.checkFriendsExist();

  }

  @Test
  void friendsTableDisplayOfRequestInFriends(@User(INVITATION_RECEIVED) UserJson user) {
    login(user.username(),user.testData().password());
    menuPage.goToTabFriends();
    friendsPage.checkingTheDisplayOfRequestInFriends();
  }

  @Test
  void allPeopleTableDisplayOfRequestInFriends(@User(INVITATION_RECEIVED) UserJson user) {
    login(user.username(),user.testData().password());
    menuPage.goToTabAllPeople();
    allPeoplePage.checkingTheDisplayOfRequestInFriends();
  }

  @Test
  void allPeopleTableDisplayOfSentFriendRequest(@User(INVITATION_SEND) UserJson user) {
    login(user.username(),user.testData().password());
    menuPage.goToTabAllPeople();
    allPeoplePage.checkingTheDisplayOfSentFriendRequest();
  }
}
