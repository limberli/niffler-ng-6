package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendsPage {
    private final ElementsCollection friendsRows = $$("#friends tr");
    private final ElementsCollection requestsRows = $$("#requests tr");
    private final ElementsCollection allPeopleRows = $$("#all tr");
    private final SelenideElement emptyFriendsMsg = $(withText("There are no users yet"));

    public FriendsPage assertThatFriendIsPresent(String friendName) {
        friendsRows.find(text(friendName)).shouldBe(visible);
        return this;
    }

    public FriendsPage shouldEmptyFriendsMsgShouldAppear() {
        emptyFriendsMsg.should(appear);
        return this;
    }

    public FriendsPage assertThatIncomingRequestIsPresent(String name) {
        friendsRows.find(text(name)).shouldBe(visible);
        return this;
    }

    public FriendsPage assertThatOutcomeRequestIsPresent(String name) {
        allPeopleRows.filterBy(text(name)).first().$(byText("Waiting...")).should(appear);
        return this;
    }
}