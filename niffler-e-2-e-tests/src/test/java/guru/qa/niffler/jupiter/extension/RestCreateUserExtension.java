package guru.qa.niffler.jupiter.extension;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.category.CategoryApiClient;
import guru.qa.niffler.api.registration.RegistrationApiClient;
import guru.qa.niffler.api.spend.SpendApiClient;
import guru.qa.niffler.api.userData.UserDataApiClient;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.*;
import guru.qa.niffler.utils.DataUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RestCreateUserExtension extends CreateUserExtension {

  private final RegistrationApiClient registrationApiClient = new RegistrationApiClient();
  private final UserDataApiClient userDataApiClient = new UserDataApiClient();
  private final SpendApiClient spendApiClient = new SpendApiClient();
  private final CategoryApiClient categoryApiClient = new CategoryApiClient();

  @Override
  public UserJson createUser(TestUser user) {
    String username = user.username().isEmpty()
            ? DataUtils.generateRandomUsername()
            : user.username();
    String password = user.password().isEmpty()
            ? "12345"
            : user.password();
    registrationApiClient.doRegistrationUser(username, password);
    Stopwatch stopwatch = Stopwatch.createStarted();
    while (stopwatch.elapsed(TimeUnit.MILLISECONDS) < 10000) {
      try {
        UserJson userJson = userDataApiClient.getCurrentUser(username);
        if (userJson != null) {
          userJson.additionTestData(new TestData(
                  password,
                  new ArrayList<>(),
                  new ArrayList<>()
          ));
          return userJson;
        } else {
          try {Thread.sleep(100);} catch (Exception ignored){}
        }
      } catch (Exception ignored){}
    }
    throw new IllegalStateException("Не удалось получить пользователя из userdata");
  }

  @Override
  public UserJson createCategory(TestUser user, UserJson createdUser) {
    try {
      createdUser.testData()
              .categoryJsons()
              .add(categoryApiClient.createCategory(
                      new CategoryJson(null,
                              DataUtils.generateNewCategory(),
                              createdUser.username())));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return createdUser;
  }

  @Override
  public UserJson createSpend(TestUser user, UserJson createdUser) {
    if (createdUser.testData().categoryJsons().isEmpty()) {
      throw new IllegalStateException("Перед созданием Spend не помешает создать Category");
    }
    try {
      createdUser.testData()
              .spendJsons()
              .add(spendApiClient.createSpend(
                      new SpendJson(null,
                              new Date(),
                              createdUser.testData().categoryJsons().get(0).category(),
                              CurrencyValues.RUB,
                              1000.00,
                              "Autogen",
                              createdUser.username())));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return createdUser;
  }

  @Override
  public void createFriends(TestUser user, UserJson createdUser) {

  }

  @Override
  public void createIncomeInvitation(TestUser user, UserJson createdUser) {

  }

  @Override
  public void createOutcomeInvitations(TestUser user, UserJson createdUser) {

  }
}
