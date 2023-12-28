package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.DisabledByIssue;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Allure;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.SpendsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTest extends BaseWebTest {

  static {
    Configuration.browserSize = "1980x1024";
  }
  protected final LoginPage loginPage = new LoginPage();
  protected final MainPage mainPage = new MainPage();
  protected final SpendsPage spendsPage = new SpendsPage();

  @GenerateCategory(
          username = "duck",
          category = "Обучение"
  )
  @GenerateSpend(
      username = "duck",
      description = "QA.GURU Advanced 4",
      amount = 72500.00,
      category = "Обучение",
      currency = CurrencyValues.RUB
  )
  @DisabledByIssue("74")
  @Test
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    Selenide.open("http://127.0.0.1:3000/main");
    mainPage.clickLogin();
    loginPage.setUsername("duck");
    loginPage.setPassword("12345");
    loginPage.clickSubmit();
    spendsPage.selectSpendByDescription(spend.description());
    Integer oldCountSpend = spendsPage.getSpendsCount();
    spendsPage.clickDeleteSelected();
    spendsPage.checkSpendsCount(oldCountSpend-1);
  }
}
