package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.CreateUserDBExtension;
import guru.qa.niffler.jupiter.DbUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(CreateUserDBExtension.class)
public class DBTest extends BaseWebTest {

  private UserAuthEntity userAuthP;

  @DbUser(username = "valentin777", password = "12345")
  @BeforeEach
  void createUserForTest(UserAuthEntity userAuth) {
    assertEquals(userAuth.getUsername(),"valentin777");
    userAuthP = userAuth;
  }

  @Test
  void statisticShouldBeVisibleAfterLogin2() {
    Selenide.open("http://127.0.0.1:3000/main");
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(userAuthP.getUsername());
    $("input[name='password']").setValue(userAuthP.getPassword());
    $("button[type='submit']").click();
    $(".main-content__section-stats").should(visible);
  }
}
