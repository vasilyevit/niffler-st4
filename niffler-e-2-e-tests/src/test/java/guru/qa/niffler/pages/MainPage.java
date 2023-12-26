package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement login = $("a[href*='redirect']");

    @Step("Нажатие кнопки Login")
    public void clickLogin(){
        login.click();
    }


}
