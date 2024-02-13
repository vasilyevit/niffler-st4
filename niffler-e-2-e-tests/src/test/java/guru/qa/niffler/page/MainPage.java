package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

  private final SelenideElement profile =  $("a[href*='profile']");

  public MainPage checkThatStatisticDisplayed() {
    return this;
  }

  public void clickProfile(){
    profile.click();
  }
}
