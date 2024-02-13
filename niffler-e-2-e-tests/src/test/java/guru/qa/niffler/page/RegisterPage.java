package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RegisterPage extends BasePage<RegisterPage> {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement confirmPasswordInput = $("#passwordSubmit");
    private final SelenideElement submitBtn = $("button.form__submit");


    public void checkForm(){
        usernameInput.should(visible);
        passwordInput.should(visible);
        confirmPasswordInput.should(visible);
        submitBtn.should(visible);
    }

    @Step("Заполняем имя пользователя: {username}")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Заполняем пароль: {password}")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Заполняем пароль повторно: {confirmPassword}")
    public RegisterPage setConfirmPassword(String confirmPassword) {
        confirmPasswordInput.setValue(confirmPassword);
        return this;
    }

    @Step("Отправка формы регистрации")
    public RegisterPage submit() {
        submitBtn.click();
        return this;
    }

}
