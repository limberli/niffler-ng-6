package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpendById(
          @Path("id") String id,                 // Path parameter
          @Query("username") String username     // Query parameter
          );

  @GET ("internal/spends/all")
  Call<List<SpendJson>> getAllSpends(
          @Query("username") String username,
          @Query("currency") CurrencyValues currency,
          @Query("from") Date from,            // Необязательный параметр (дата)
          @Query("to") Date to                 // Необязательный параметр (дата)
          );


  @DELETE("internal/spends/remove")
  Call<SpendJson> removeSpend(
          @Query("username") String username,      // Обязательный параметр username
          @Query("ids") List<String> ids           // Параметры ids для удаления
  );

  @POST ("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH ("internal/categories/update")
  Call<CategoryJson> editCategory(@Body CategoryJson category);

  @GET ("internal/categories/all")
  Call<List<CategoryJson>> getAllCategories(
          @Query("username") String username,
          @Query("archived") Boolean archived
  );

}