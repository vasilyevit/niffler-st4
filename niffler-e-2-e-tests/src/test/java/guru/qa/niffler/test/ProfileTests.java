package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static guru.qa.niffler.page.message.ErrorMsg.CATEGORY_MSG;

public class ProfileTests extends BaseWebTest {

  static {
    Configuration.browserSize = "1980x1024";
  }

  @BeforeEach
  void doLogin() {
    Selenide.open("http://127.0.0.1:3000/main");
    $("a[href*='redirect']").click();
    loginPage
            .setLogin("duck")
            .setPassword("12345")
            .submit();
    mainPage.checkThatStatisticDisplayed();
  }

  @Test
  void testProfileForm(){
    mainPage.clickProfile();
    profilePage.checkForm();
  }

  @Test
  void testCheckName(){
    mainPage.clickProfile();
    profilePage.checkFirstname("D");
  }

  @Test
  void testCheckSurname(){
    mainPage.clickProfile();
    profilePage.checkSurname("U");
  }

  @Test
  void testCreateCategoryFail(){
    mainPage.clickProfile();
    profilePage.setCategory("Обучение");
    profilePage.createCategory();
    profilePage.checkMessage(CATEGORY_MSG);
  }



}
