package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {

    private final SelenideElement profileTitle = $(withText("Profile"));
    private final SelenideElement showArchivedButton = $(".MuiSwitch-switchBase");

    public ProfilePage waitProfileLoaded() {
        profileTitle.should(appear);
        return this;
    }

    public ProfilePage clickShowArchived() {
        showArchivedButton.click();
        return this;
    }

    public ProfilePage checkArchivedCategoryExists(String category) {
        $x("//span[text()=" + "\"" + category + "\"" + "]").shouldBe(visible);
        return this;
    }

    public ProfilePage checkCategoryExists(String category) {
        $x("//span[text()=" + "\"" + category + "\"" + "]").shouldBe(visible);
        return this;
    }

}