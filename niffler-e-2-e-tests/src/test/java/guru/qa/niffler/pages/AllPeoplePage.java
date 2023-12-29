package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class AllPeoplePage {

    @Step("Проверка отображения полученного запроса в друзья")
    public void checkingTheDisplayOfRequestInFriends() {
        $x("//tbody//*[@data-tooltip-content='Submit invitation']")
                .shouldBe(Condition.exist.because("Отсутствует полученный запрос в друзья"));
    }

    @Step("Проверка отображения отправленного запроса в друзья")
    public void checkingTheDisplayOfSentFriendRequest() {
        $x("//tbody//*[text()='Pending invitation']")
                .shouldBe(Condition.exist.because("Отсутствует отправленный запрос в друзья"));
    }
}
