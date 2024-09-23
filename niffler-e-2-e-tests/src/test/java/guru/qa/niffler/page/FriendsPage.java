package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class FriendsPage {
    private final ElementsCollection friendsRows = $$("#friends tr");
    private final ElementsCollection requestsRows = $$("#requests tr");
    private final ElementsCollection allPeopleRows = $$("#all tr");
    private final SelenideElement emptyFriendsMsg = $(withText("There are no users yet"));

    public FriendsPage assertThatFriendIsPresent(String friendName) {
        friendsRows.filterBy(text(friendName)).first().should(appear);
        return this;
    }

    public FriendsPage emptyFriendsMsgShouldAppear() {
        emptyFriendsMsg.should(appear);
        return this;
    }

    public FriendsPage assertThatIncomingRequestIsPresent(String name) {
        requestsRows.filterBy(text(name)).first().should(appear);
        return this;
    }

    public FriendsPage assertThatOutcomeRequestIsPresent(String name) {
        allPeopleRows.filterBy(text(name)).first().$(byText("Waiting...")).should(appear);
        return this;
    }
}