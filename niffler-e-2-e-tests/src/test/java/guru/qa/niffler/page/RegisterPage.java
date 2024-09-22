package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static java.lang.String.format;


public class RegisterPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitPasswordInput = $("input[name='passwordSubmit']");
    private final SelenideElement signUpButton = $("button[type='submit']");
    private final SelenideElement singInButton = $(".form_sign-in");
    private final SelenideElement fromError = $(".form__error");
    private final SelenideElement successRegistrationMsg = $(withText("Congratulations! You've registered!"));

    private static final String userAlreadyExistErrorMsgStr = "Username `%s` already exists";

    public RegisterPage setUsername(String username){
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password){
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setSubmitPassword(String submitPassword){
        submitPasswordInput.setValue(submitPassword);
        return this;
    }

    public RegisterPage clickSignUpButton(){
        signUpButton.click();
        return this;
    }

    public RegisterPage clickSignInButton(){
        singInButton.click();
        return this;
    }

    public RegisterPage shouldSuccessRegister(){
        successRegistrationMsg.should(appear);
        return this;
    }

    public RegisterPage shouldErrorRegister(String value){
        fromError.shouldHave(text(value));
        return this;
    }

    public RegisterPage userAlreadyExistErrorShouldAppear(String username) {
        $(withText(format(userAlreadyExistErrorMsgStr, username))).should(appear);
        return this;
    }

}