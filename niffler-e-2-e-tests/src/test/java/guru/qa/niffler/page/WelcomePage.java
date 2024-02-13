package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage> {

    private final SelenideElement login = $("a[href*='redirect']");
    private final SelenideElement registerBtn = $("a[href*='/register']");

    @Step("Нажатие кнопки Вход")
    public void clickLogin(){
        login.click();
    }

    @Step("Нажатие на кнопку Регистрация")
    public void register() {
        registerBtn.click();
    }

    public void checkForm(){
        login.should(Condition.visible);
        registerBtn.should(Condition.visible);
    }
}
