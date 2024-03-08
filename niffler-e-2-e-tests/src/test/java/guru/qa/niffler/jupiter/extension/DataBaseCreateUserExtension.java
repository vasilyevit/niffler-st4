package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.*;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.DataUtils;

import java.util.Arrays;

public class DataBaseCreateUserExtension extends CreateUserExtension {

  private static UserRepository userRepository = new UserRepositoryHibernate();
  private static CategoryRepository categoryRepository = new CategoryRepositoryHibernate();
  private static SpendRepository spendRepository = new SpendRepositoryHibernate();

  @Override
  public UserJson createUser(TestUser user) {
    String username = user.username().isEmpty()
        ? DataUtils.generateRandomUsername()
        : user.username();
    String password = user.password().isEmpty()
        ? "12345"
        : user.password();

    UserAuthEntity userAuth = new UserAuthEntity();
    userAuth.setUsername(username);
    userAuth.setPassword(password);
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);
    AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);

    userAuth.addAuthorities(authorities);

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setCurrency(CurrencyValues.RUB);

    userRepository.createInAuth(userAuth);
    userRepository.createInUserdata(userEntity);
    return new UserJson(
        userEntity.getId(),
        userEntity.getUsername(),
        userEntity.getFirstname(),
        userEntity.getSurname(),
        guru.qa.niffler.model.CurrencyValues.valueOf(userEntity.getCurrency().name()),
        userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
        null,
        new TestData(
            password,
            null,
                null
        )
    );
  }

  @Override
  public UserJson createCategory(TestUser user, UserJson createdUser) {
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setUsername(createdUser.username());
    categoryEntity.setCategory(DataUtils.generateNewCategory());
    createdUser.testData().categoryJsons().add(CategoryJson.fromEntity(categoryRepository.createCategory(categoryEntity)));
    return createdUser;
  }

  @Override
  public UserJson createSpend(TestUser user, UserJson createdUser) {
    if (createdUser.testData().categoryJsons().isEmpty()) {
      throw new IllegalStateException("Перед созданием Spend не помешает создать Category");
    }
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setUsername(createdUser.username());
    categoryEntity.setCategory(createdUser.testData().categoryJsons().get(0).category());

    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setUsername(createdUser.username());
    spendEntity.setCategory(categoryEntity);
    spendEntity.setCurrency(guru.qa.niffler.model.CurrencyValues.RUB);
    spendEntity.setAmount(1000.00);
    spendEntity.setDescription("Autogen db");
    createdUser.testData().spendJsons().add(SpendJson.fromEntity(spendRepository.createSpend(spendEntity)));
    return createdUser;
  }
}
