package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Header {

    private final SelenideElement self = $("#root header");
    private final ElementsCollection menu = $$("li[role='menuitem']");


    @Step("Перейти на страницу профиля")
    public ProfilePage toProfilePage() {
        self.$("button").click();
        menu.findBy(Condition.text("Profile")).click();
        return new ProfilePage();
    }

    @Step("Перейти на страницу друзей")
    public ProfilePage toFriendsPage() {
        self.$("button").click();
        menu.findBy(Condition.text("Friends")).click();
        return new ProfilePage();
    }

    @Step("Перейти на страницу всех людей")
    public ProfilePage toAllPeoplePage() {
        self.$("button").click();
        menu.findBy(Condition.text("All People")).click();
        return new ProfilePage();
    }

    @Step("Выйти из аккаунта")
    public LoginPage signOut() {
        self.$("button").click();
        menu.findBy(Condition.text("Sign out")).click();
        return new LoginPage();
    }

    @Step("Добавить новую трату")
    public EditSpendingPage addSpendingPage() {
        self.$(byText("New spending")).click();
        return new EditSpendingPage();
    }

    @Step("Перейти на главную страницу")
    public MainPage toMainPage() {
        self.$("h1").click();
        return new MainPage();
    }

}