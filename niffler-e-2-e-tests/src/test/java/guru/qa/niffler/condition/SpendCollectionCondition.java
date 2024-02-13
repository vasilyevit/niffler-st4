package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ex.DoesNotContainTextsError;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.TextsMismatch;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpendCollectionCondition {



  public static CollectionCondition spends(SpendJson... expectedSPends) {
    return new CollectionCondition() {
      protected List<String> expectedTexts = new ArrayList<>();

      @Override
      public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
        List<String> actualTexts = lastCheckResult.getActualValue();
        if (actualTexts == null || actualTexts.isEmpty()) {
          throw new ElementNotFound(collection, toString(), timeoutMs, cause);
        }
        else {
          String message = lastCheckResult.getMessageOrElse(() -> "Texts mismatch");
          throw new TextsMismatch(message, collection, expectedTexts, actualTexts, explanation, timeoutMs, cause);
        }
      }

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedSPends.length) {
          return CheckResult.rejected("Incorrect table size", elements);
        }

        List<String> spendRows = new ArrayList<>();
        for (WebElement element : elements) {
          List<WebElement> tds = element.findElements(By.cssSelector("td"));
          spendRows.add("amount: " + tds.get(2).getText() +
                  ", currency: " + tds.get(3).getText() +
                  ", category: " + tds.get(4).getText() +
                  ", description: " + tds.get(5).getText());
        }
        for (WebElement element : elements) {

          List<WebElement> tds = element.findElements(By.cssSelector("td"));

          boolean checkPassed = true;

          for (SpendJson expectedSPend : expectedSPends) {
            expectedTexts = List.of("amount: " + expectedSPend.amount() +
                    ", currency: " + expectedSPend.currency().name() +
                    ", category: " + expectedSPend.category() +
                    ", description: " + expectedSPend.description());
            if (Double.parseDouble(tds.get(2).getText()) != expectedSPend.amount() ||
                    !tds.get(3).getText().equals(expectedSPend.currency().name()) ||
                    !tds.get(4).getText().equals(expectedSPend.category()) ||
                    !tds.get(5).getText().equals(expectedSPend.description())) {
              checkPassed = false;
            }
            if (checkPassed) {
              break;
            }
          }

          if (checkPassed) {
            return CheckResult.accepted();
          } else {
            return CheckResult.rejected("Incorrect spends content", spendRows);
          }
        }


        return super.check(driver, elements);
      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }
    };

  }
}
