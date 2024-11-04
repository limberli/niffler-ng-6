package guru.qa.niffler.data.mapper;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.impl.sjdbc.CategoryDaoSpringJdbc;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();
    private static final CategoryDao categoryDao =
            new CategoryDaoSpringJdbc(Databases.dataSource(Config.getInstance().spendJdbcUrl()));


    private SpendEntityRowMapper() {
    }

    @Override
    public SpendEntity mapRow(@Nonnull ResultSet rs, int rowNum) throws SQLException {
        SpendEntity result = new SpendEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setUsername(rs.getString("username"));
        result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        result.setSpendDate(rs.getDate("spend_date"));
        result.setAmount(rs.getDouble("amount"));
        result.setDescription(rs.getString("description"));
        result.setCategory(categoryDao.findCategoryById(UUID.fromString(rs.getString("category_id"))).get());
        return result;
    }

}