package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.pages.*;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }

    protected final LoginPage loginPage = new LoginPage();
    protected final MainPage mainPage = new MainPage();
    protected final SpendsPage spendsPage = new SpendsPage();
    protected final FriendsPage friendsPage = new FriendsPage();
    protected final AllPeoplePage allPeoplePage = new AllPeoplePage();

    @Step("Переход на вкладку All People")
    public void goToTabAllPeople() {
        $x("//li[@data-tooltip-id='people']").shouldBe(visible).click();
    }

    @Step("Переход на вкладку Friends")
    public void goToTabFriends() {
        $x("//li[@data-tooltip-id='friends']").shouldBe(visible).click();
    }

    @Step("Переход на вкладку Main")
    public void goToTabMain() {
        $x("//li[@data-tooltip-id='main']").shouldBe(visible).click();
    }

    @Step("Авторизация с учетными данными {username}:{password}")
    public void login(String username, String password) {
        Selenide.open("http://127.0.0.1:3000/main");
        mainPage.clickLogin();
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickSubmit();
    }


}
