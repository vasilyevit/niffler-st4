package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {
    protected final WelcomePage welcomePage = new WelcomePage();
    protected final FriendsPage friendsPage = new FriendsPage();
    protected final LoginPage loginPage = new LoginPage();
    protected final MainPage mainPage = new MainPage();
    protected final ProfilePage profilePage = new ProfilePage();
    protected final RegisterPage registerPage = new RegisterPage();
}
