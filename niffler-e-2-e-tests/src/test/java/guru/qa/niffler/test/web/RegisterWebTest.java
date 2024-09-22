package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.utils.RandomData.generateUsername;

@ExtendWith(BrowserExtension.class)
public class RegisterWebTest {

    private static final Config CFG = Config.getInstance();
    private static final String defPassword = "Dr12kRt";

    @Test
    @DisplayName("Регистрация нового пользователя")
    void shouldRegisterNewUser() {
        open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .setUsername(generateUsername()).setPassword(defPassword).setSubmitPassword(defPassword)
                .clickSignUpButton()
                .shouldSuccessRegister();
    }

    @Test
    @DisplayName("Регистрация пользователя с существующим именем")
    void shouldNotRegisterUserWithExistingUsername() {
        String existingUsername = "artem";

        open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .setUsername(existingUsername).setPassword(defPassword).setSubmitPassword(defPassword)
                .clickSignUpButton()
                .userAlreadyExistErrorShouldAppear(existingUsername);
    }

    @Test
    @DisplayName("Проверка отображения ошибки при несоответствии пароля и подтверждения")
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String passwordAreNotEqual = "12345";
        String errorMsg = "Passwords should be equal";

        open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .setUsername(generateUsername()).setPassword(defPassword).setSubmitPassword(passwordAreNotEqual)
                .clickSignUpButton()
                .shouldErrorRegister(errorMsg);

    }

    @Test
    @DisplayName("Переход на главную страницу после успешной авторизации")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        String username = "artem";
        String password = "12345";

        open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .waitMainPageLoaded();

    }

    @Test
    @DisplayName("Оставаться на странице входа при вводе неправильных данных")
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(){
        String username = "artem";

        open(CFG.frontUrl(), LoginPage.class)
                .setUsername(username).setPassword(defPassword)
                .clickSubmitButton()
                .checkErrorInvalidUserCredentialsShouldAppear();
    }

}