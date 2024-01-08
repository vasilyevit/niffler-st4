package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.*;

@ExtendWith(UsersQueueExtension.class)
public class FriendsForEachTest extends BaseWebTest  {

  @BeforeEach
  void doLogin(@User(INVITATION_RECEIVED) UserJson user) {
    login(user.username(),user.testData().password());
  }

  @Test
  void friendsTableDisplayOfRequestInFriends() {
    menuPage.goToTabFriends();
    friendsPage.checkingTheDisplayOfRequestInFriends();
  }

  @Test
  void allPeopleTableDisplayOfRequestInFriends() {
    menuPage.goToTabAllPeople();
    allPeoplePage.checkingTheDisplayOfRequestInFriends();
  }
}
