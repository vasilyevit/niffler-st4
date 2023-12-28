package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SpendsPage {

    private final SelenideElement deleteSelected =$(byText("Delete selected"));
    private final ElementsCollection spends =$$(".spendings-table tbody tr");

    @Step("Выбрать расход по описанию {description}")
    public void selectSpendByDescription(String description){
        $(".spendings-table tbody")
                .$$("tr")
                .find(text(description))
                .$("td")
                .scrollIntoView(true)
                .click();
    }

    @Step("Нажатие кнопки Delete selected")
    public void clickDeleteSelected(){
        deleteSelected.click();
    }

    public Integer getSpendsCount(){
        return spends.size();
    }

    public void checkSpendsCount(Integer count){
        spends.shouldHave(size(count));
    }

}
