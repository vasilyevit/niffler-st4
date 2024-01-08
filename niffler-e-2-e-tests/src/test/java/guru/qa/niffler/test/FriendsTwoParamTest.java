package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.*;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTwoParamTest extends BaseWebTest  {

  @Test
  void allPeopleTableDisplayOfSentFriendRequest2(@User(INVITATION_RECEIVED) UserJson user, @User(INVITATION_SEND) UserJson userRec) {
    login(user.username(),user.testData().password());
    menuPage.goToTabFriends();
    friendsPage.checkUserHaveRequest(userRec.username());
  }

}
