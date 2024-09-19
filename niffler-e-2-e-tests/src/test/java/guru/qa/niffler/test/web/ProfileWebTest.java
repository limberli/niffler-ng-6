package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;


public class ProfileWebTest {

    private static final Config CFG = Config.getInstance();
    private static final String defPassword = "12345";

    @Category(
            username = "artem",
            archived = true
    )

    @Test
    @DisplayName("Проверка отображения архивной категории в списке категорий")
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), defPassword)
                .waitMainPageLoaded().openProfileUrl().clickShowArchived()
                .checkArchivedCategoryExists(category.name());
    }





    @Category(
            username = "artem",
            archived = false
    )

    @Test
    @DisplayName("Проверка отображения активной категории в списке категорий")
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), defPassword)
                .waitMainPageLoaded().openProfileUrl()
                .checkCategoryExists(category.name());
    }

}