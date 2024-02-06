package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsPage extends BasePage<FriendsPage> {

    private final SelenideElement tableWithFriend = $x("//tbody");

    @Step("Есть запись о друге")
    public FriendsPage checkFriendsExist() {
        $x("//tbody//*[text()='You are friends']")
                .shouldBe(exist.because("Отсутствует запись о друге"));
        return this;
    }

    @Step("Проверка отображения запроса в друзья")
    public FriendsPage checkingTheDisplayOfRequestInFriends() {
        tableWithFriend.$$x(".//tr")
                .shouldHave(sizeGreaterThan(0)
                        .because("Отсутствуют друзья или запросы в друзья"));

        tableWithFriend.$x(".//div[@data-tooltip-content = 'Submit invitation']")
                .shouldBe(exist.because("Отсутствует кнопка подтверждения дружбы"));

        tableWithFriend.$x(".//div[@data-tooltip-content = 'Decline invitation']")
                .shouldBe(exist.because("Отсутствует кнопка отклонения дружбы"));
        return this;
    }


    @Step("Проверка наличия запроса от пользователя {username}")
    public FriendsPage checkUserHaveRequest(String username) {
        $$x("//tbody//tr[.//*[@data-tooltip-id='submit-invitation']]")
                .filter(text(username))
                .shouldHave(size(1)
                        .because("Отсутствует запрос от пользователя " + username));
        return this;
    }
}
