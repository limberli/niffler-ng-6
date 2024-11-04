package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;


import javax.annotation.Nonnull;

import static guru.qa.niffler.utils.RandomData.randomCategoryName;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();



    @Override
    public void beforeEach(@Nonnull ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                //ищет аннотацию @User
                .ifPresent(userAnno -> {
                    if (userAnno.categories().length > 0) {
                        Category anno = userAnno.categories()[0];
                        CategoryJson category = new CategoryJson(
                                null,
                                randomCategoryName(),
                                userAnno.username(),
                                anno.archived()
                        );

                        CategoryEntity createdCategory = CategoryEntity.fromJson(spendDbClient.createCategoryIfNotExist(category));

                        context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
                    }
                });
    }

    @Override
    public boolean supportsParameter(@Nonnull ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, @Nonnull ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return extensionContext.getStore(SpendingExtension.NAMESPACE)
                .get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(@Nonnull ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null) {
            spendDbClient.deleteCategory(category);
        }
    }

}