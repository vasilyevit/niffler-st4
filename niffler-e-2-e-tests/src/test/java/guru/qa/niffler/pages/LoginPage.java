package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement login = $("a[href*='redirect']");
    private final SelenideElement username = $("input[name='username']");
    private final SelenideElement password = $("input[name='password']");
    private final SelenideElement submitForm =  $("button[type='submit']");

    @Step("Нажатие кнопки Login")
    public void clickLogin(){
        login.click();
    }

    @Step("Заполнить логин пользователя значением {value}")
    public void setUsername(String value){
        username.setValue(value);
    }

    @Step("Заполнить пароль пользователя значением {pass}")
    public void setPassword(String pass){
        password.setValue(pass);
    }

    @Step("Нажатие кнопки submit")
    public void clickSubmit(){
        submitForm.click();
    }

}
