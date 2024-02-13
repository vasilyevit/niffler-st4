package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;

public class unAuthTest extends BaseWebTest {

  static {
    Configuration.browserSize = "1980x1024";
  }

  @Test
  void testWelcomeForm(){
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.checkForm();
  }

  @Test
  void testRegisterForm(){
    Selenide.open("http://127.0.0.1:9000/register");
    registerPage.checkForm();
  }

  @Test
  void testLoginForm(){
    Selenide.open("http://127.0.0.1:9000/login");
    loginPage.checkForm();
  }


}
