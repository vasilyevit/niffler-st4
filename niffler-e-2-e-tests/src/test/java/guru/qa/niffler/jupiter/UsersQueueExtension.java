package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.User.UserType.*;
import static java.util.Arrays.asList;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  private static final Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> commonQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> invitationSend = new ConcurrentLinkedQueue<>();
    Queue<UserJson> invitationReceived= new ConcurrentLinkedQueue<>();
    friendsQueue.add(user("duck", "12345", WITH_FRIENDS));
    friendsQueue.add(user("alex", "12345", WITH_FRIENDS));
    commonQueue.add(user("barsik", "12345", COMMON));
    invitationSend.add(user("cat", "12345", INVITATION_SEND));
    invitationSend.add(user("tom", "12345", INVITATION_SEND));
    invitationReceived.add(user("mouse", "12345", INVITATION_RECEIVED));
    invitationReceived.add(user("jerry", "12345", INVITATION_RECEIVED));
    USERS.put(WITH_FRIENDS, friendsQueue);
    USERS.put(COMMON, commonQueue);
    USERS.put(INVITATION_SEND, invitationSend);
    USERS.put(INVITATION_RECEIVED, invitationReceived);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    List<Parameter> parametersDraft = new ArrayList<>();
    parametersDraft.addAll(asList(context.getRequiredTestMethod().getParameters()));
    parametersDraft.addAll(Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(BeforeEach.class))
            .map(Executable::getParameters)
            .flatMap(Arrays::stream)
            .toList());

    List<Parameter> parameters = parametersDraft.stream()
            .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
            .filter(parameter -> parameter.isAnnotationPresent(User.class))
            .toList();

    Map<Pair<String, User.UserType>, UserJson> testCandidates = new ConcurrentHashMap<>();
    for (Parameter parameter : parameters) {
      User annotation = parameter.getAnnotation(User.class);
      if (annotation != null && parameter.getType().isAssignableFrom(UserJson.class)) {
        UserJson testCandidate = null;
        Queue<UserJson> queue = USERS.get(annotation.value());
        while (testCandidate == null) {
          testCandidate = queue.poll();
        }
        testCandidates.put(Pair.of(parameter.getName(), annotation.value()), testCandidate);
        context.getStore(NAMESPACE).put(context.getUniqueId(), testCandidates);
      }
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Map<Pair<String, User.UserType>, UserJson> usersFromTest = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
    for (Pair<String, User.UserType> userType : usersFromTest.keySet()) {
      USERS.get(userType.getRight()).add(usersFromTest.get(userType));
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter()
        .getType()
        .isAssignableFrom(UserJson.class) &&
        parameterContext.getParameter().isAnnotationPresent(User.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    User.UserType userType = parameterContext.getParameter().getAnnotation(User.class).value();
    Pair<String, User.UserType> key = Pair.of(parameterContext.getParameter().getName(), userType);
    return (UserJson) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class).get(key);
  }

  private static UserJson user(String username, String password, User.UserType userType) {
    return new UserJson(
        null,
        username,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        new TestData(
            password,
            userType
        )
    );
  }
}
