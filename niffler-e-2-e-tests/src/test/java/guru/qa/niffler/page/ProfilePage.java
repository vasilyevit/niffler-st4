package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage extends BasePage<ProfilePage> {

    private final SelenideElement editAvatarBtn = $("button.profile__avatar-edit");
    private final SelenideElement uploadAvatar = $(".edit-avatar__input[type=file]");
    private final SelenideElement avatar = $("img.profile__avatar");
    private final SelenideElement firstname = $("[name=firstname]");
    private final SelenideElement surname = $("[name=surname]");
    private final SelenideElement submitBtn = $("button[type=submit]");
    private final SelenideElement category = $("[name=category]");
    private final SelenideElement addCategoryBtn = $(".add-category__input-container button");
    private final ElementsCollection categories = $$(".categories__list .categories__item");



    @Step("Upload avatar")
    public ProfilePage uploadAvatarFromClasspath(String classpath) {
        editAvatarBtn.click();
        uploadAvatar.uploadFromClasspath(classpath);
        return this;
    }

    @Step("Заполняем имя: {firstname}")
    public ProfilePage setFirstname(String firstname) {
        this.firstname.setValue(firstname);
        return this;
    }

    @Step("Заполняем фамилию: {surname}")
    public ProfilePage setSurname(String surname) {
        this.surname.setValue(surname);
        return this;
    }

//    @Step("Выбираем валюту: {currency}")
//    public ProfilePage setCurrency(String currency) {
//        currencySelect.selectOption(currency);
//        return this;
//    }

    @Step("Submit profile data")
    public ProfilePage submitData() {
        submitBtn.scrollTo().click();
        return this;
    }

    @Step("Выбираем категорию: {category}")
    public ProfilePage setCategory(String category) {
        this.category.setValue(category);
        return this;
    }

    @Step("Новая категория")
    public ProfilePage createCategory() {
        addCategoryBtn.click();
        return this;
    }

    @Step("Проверяем, что имя равно : {firstname}")
    public ProfilePage checkFirstname(String firstname) {
        this.firstname.shouldHave(value(firstname));
        return this;
    }

    @Step("Проверяем, что фамилия равна: {surname}")
    public ProfilePage checkSurname(String surname) {
        this.surname.shouldHave(value(surname));
        return this;
    }

    public void checkForm() {
        avatar.should(visible);
        firstname.should(visible);
        surname.should(visible);
    }
//
//    @Step("Проверяем, что валюта равно: {currency}")
//    public ProfilePage checkCurrency(String currency) {
//        currencySelect.checkSelectedOption(currency);
//        return this;
//    }

}
