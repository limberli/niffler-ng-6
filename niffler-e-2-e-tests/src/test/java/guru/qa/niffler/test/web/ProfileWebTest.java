package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Flaky;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class ProfileWebTest {

    private static final Config CFG = Config.getInstance();
    private static final String defPassword = "12345";

    @User(
            username = "artem",
            categories = @Category(archived = true))
    @Test
    @DisplayName("Проверка отображения архивной категории в списке категорий")
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), defPassword)
                .waitMainPageLoaded()
                .openProfileUrl().clickShowArchived()
                .checkCategoryExists(category.name());
    }


    @User(
            username = "artem",
            categories = @Category(archived = false))
    @Test
    @DisplayName("Проверка отображения активной категории в списке")
    @Flaky
    // 409 ошибка
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), defPassword)
                .waitMainPageLoaded().openProfileUrl()
                .checkCategoryExists(category.name());
    }

}