package guru.qa.niffler.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class MenuPage {

    @Step("Переход на вкладку All People")
    public void goToTabAllPeople() {
        $x("//li[@data-tooltip-id='people']").click();
    }

    @Step("Переход на вкладку Friends")
    public void goToTabFriends() {
        $x("//li[@data-tooltip-id='friends']").click();
    }

    @Step("Переход на вкладку Main")
    public void goToTabMain() {
        $x("//li[@data-tooltip-id='main']").click();
    }
}
