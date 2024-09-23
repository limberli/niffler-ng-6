package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Проверка наличия друга в таблице друзей")
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable (@UserType(WITH_FRIEND) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .waitMainPageLoaded()
                .openFriendsViaUrl()
                .assertThatFriendIsPresent(user.friend());

    }

    @Test
    @DisplayName("Проверка отсутствия друзей у нового пользователя")
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser (@UserType(EMPTY) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .waitMainPageLoaded().openFriendsViaUrl()
                .emptyFriendsMsgShouldAppear();
    }

    @Test
    @DisplayName("Проверка наличия полученного приглашения")
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable (@UserType (WITH_INCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .waitMainPageLoaded().openFriendsViaUrl()
                .assertThatIncomingRequestIsPresent(user.income());
    }

    @Test
    @DisplayName("Проверка наличи отправленного приглашения")
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .waitMainPageLoaded().openAllPeopleViaUrl()
                .assertThatOutcomeRequestIsPresent(user.outcome());
    }
}