package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CreateUserDBExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateUserDBExtension.class);

    private final UserRepositoryExtension userRepositoryExtension = new UserRepositoryExtension();
    private UserRepository userRepository;

    Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        List<Method> methods = new ArrayList<>();
        methods.add(context.getRequiredTestMethod());
        methods.addAll(Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .toList());

        userRepositoryExtension.postProcessTestInstance(this, context);
        Map<String, Object> userData = new ConcurrentHashMap<>();
        for (Method method : methods){
            DbUser annotation = method.getAnnotation(DbUser.class);
            if (annotation != null){
                String username = annotation.username().isEmpty() ? faker.name().firstName() : annotation.username();
                String password = annotation.password().isEmpty() ? "12345" : annotation.password();

                UserAuthEntity userAuthDB = new UserAuthEntity();
                userAuthDB.setUsername(username);
                userAuthDB.setPassword(password);
                userAuthDB.setEnabled(true);
                userAuthDB.setAccountNonExpired(true);
                userAuthDB.setAccountNonLocked(true);
                userAuthDB.setCredentialsNonExpired(true);
                userAuthDB.setAuthorities(Arrays.stream(Authority.values())
                        .map(e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setAuthority(e);
                            return ae;
                        }).toList()
                );

                UUID createdUserIdAuth = userRepository.createInAuth(userAuthDB).getId();
                UserEntity userdataDB = new UserEntity();
                userdataDB.setUsername(username);
                UUID createdUserIdUserdata = userRepository.createInUserdata(userdataDB).getId();

                userData.put("userAuthDB", userAuthDB);
                userData.put("createdUserIdAuth", createdUserIdAuth);
                userData.put("userdataDB", userdataDB);
                userData.put("createdUserIdUserdata", createdUserIdUserdata);
        }
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), userData);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Map userDataMap = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        UUID createdUserIdAuth = (UUID) userDataMap.get("createdUserIdAuth");
        UUID createdUserIdUserdata = (UUID) userDataMap.get("createdUserIdUserdata");
        if (createdUserIdAuth != null) {
            userRepository.deleteInAuthById(createdUserIdAuth);
        }
        if (createdUserIdUserdata != null) {
            userRepository.deleteInUserdataById(createdUserIdUserdata);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map users = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return users.get("userAuthDB");
    }
}
