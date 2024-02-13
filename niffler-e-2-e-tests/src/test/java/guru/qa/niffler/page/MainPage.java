package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.condition.PhotoCondition.photoFromClasspath;

public class MainPage extends BasePage<MainPage> {

  private final SelenideElement avatar = $(".header__avatar");
  private final SelenideElement statistics = $(".main-content__section.main-content__section-stats");
  private final SpendingTable spendingTable = new SpendingTable();
  private final SelenideElement todayFilter = $x("//button[text()='Today']");
  private final SelenideElement lastWeekFilter = $x("//button[text()='Last week']");
  private final SelenideElement lastMonthFilter = $x("//button[text()='Last month']");
  private final SelenideElement lastTimeFilter = $x("//button[text()='All time']");
  private final SelenideElement deleteSelectedBtn = $x("//button[text()='Delete selected']");

  public MainPage checkThatStatisticDisplayed() {
    statistics.should(visible);
    return this;
  }

  @Step("check avatar")
  public MainPage checkAvatar(String imageName) {
    avatar.shouldHave(photoFromClasspath(imageName));
    return this;
  }

  public SpendingTable getSpendingTable() {
    return spendingTable;
  }

  @Step("Select filter Today")
  public MainPage clickFilterToday() {
    todayFilter.click();
    return this;
  }

  @Step("Select filter Last week")
  public MainPage clickFilterLastWeek() {
    lastWeekFilter.click();
    return this;
  }

  @Step("Select filter Last month")
  public MainPage clickFilterLastMonth() {
    lastMonthFilter.click();
    return this;
  }

  @Step("Select filter All time")
  public MainPage clickFilterLastTime() {
    lastTimeFilter.click();
    return this;
  }

  @Step("Click button Delete selected")
  public MainPage clickDeleteSelected() {
    deleteSelectedBtn.click();
    return this;
  }
}
