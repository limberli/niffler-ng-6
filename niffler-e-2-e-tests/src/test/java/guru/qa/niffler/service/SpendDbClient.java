package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.impl.SpendDaoJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );

    }

    public Optional<SpendJson> findSpendById(UUID id) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findSpendById(id).map(SpendJson::fromEntity);
                }, CFG.spendJdbcUrl()
        );
    }

    public List<SpendEntity> findAllSpendsByUsername(String username) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findAllByUsername(username);
                },
                CFG.spendJdbcUrl()
        );
    }

    public void deleteSpend(SpendEntity spend) {
        transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(SpendJson.fromEntity(spend));
                    new SpendDaoJdbc(connection).deleteSpend(spendEntity);
                },
                CFG.spendJdbcUrl()
        );
    }


    public CategoryEntity createCategoryIfNotExist(CategoryJson category) {
        return transaction(connection -> {
            CategoryDaoJdbc categoryDao = new CategoryDaoJdbc(connection);
            Optional<CategoryEntity> existingCategory = categoryDao.findCategoryByUsernameAndCategoryName(category.username(), category.name());
            if (existingCategory.isPresent()) {
                return CategoryJson.fromEntity(existingCategory.get());
            } else {
                CategoryEntity categoryEntity = CategoryJson.fromEntity(category);
                return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
            }
        }, CFG.spendJdbcUrl());
    }


    public CategoryEntity createCategory(CategoryJson category) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                            .create(CategoryJson.fromEntity(category));
                    return CategoryJson.fromEntity(categoryEntity);
                },
                CFG.spendJdbcUrl()
        );
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryById(id);
                },
                CFG.spendJdbcUrl()
        );
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName);
                },
                CFG.spendJdbcUrl()
        );
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
                },
                CFG.spendJdbcUrl()
        );
    }

}