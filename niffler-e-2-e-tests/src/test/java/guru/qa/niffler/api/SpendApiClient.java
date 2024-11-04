package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Step("Send POST(\"internal/spends/add\") to niffler-spend")
    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    @Step("Send PATCH(\"/internal/spends/edit\") to niffler-spend")
    public SpendJson editSpend(SpendJson spend) {
      final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Send GET(\"/internal/spends/{id}\") to niffler-spend")
    public SpendJson getSpend(String id, String username) {
      final Response<SpendJson> response;
        try {
            response = spendApi.getSpendById(id, username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Send GET(\"/internal/spends/all\") to niffler-spend")
    public List<SpendJson> getSpends(String username, CurrencyValues cur, Date from, Date to) {
      final Response<List<SpendJson>> response;
        try {
            response = spendApi.getAllSpends(username, cur, from, to).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Send DELETE(\"/internal/spends/remove\") to niffler-spend")
    public Void removeSpend(String username, List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi.removeSpend(username, ids).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Send POST(\"/internal/categories/add\") to niffler-spend")
    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Send PATCH(\"/internal/categories/update\") to niffler-spend")
    public CategoryJson editCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.editCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Send GET(\"/internal/categories/all\") to niffler-spend")
    public List<CategoryJson> getCategories(String username, Boolean archived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getAllCategories(username, archived).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }
}