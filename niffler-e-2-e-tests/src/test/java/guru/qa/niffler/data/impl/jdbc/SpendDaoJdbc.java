package guru.qa.niffler.data.impl.jdbc;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private final Connection connection;

    public SpendDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SpendEntity create(@Nonnull SpendEntity spend) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                            "VALUES ( ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, spend.getUsername());
                ps.setDate(2, spend.getSpendDate());
                ps.setString(3, spend.getCurrency().name());
                ps.setDouble(4, spend.getAmount());
                ps.setString(5, spend.getDescription());
                ps.setObject(6, spend.getCategory().getId());
                ps.executeUpdate();
                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can`t find id in ResultSet");
                    }
                }
                spend.setId(generatedKey);
                return spend;
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        SpendEntity se = new SpendEntity();
                        se.setId(rs.getObject("id", UUID.class));
                        se.setUsername(rs.getString("username"));
                        se.setSpendDate(rs.getDate("spend_date"));
                        se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        se.setAmount(rs.getDouble("amount"));
                        se.setDescription(rs.getString("description"));
                        new CategoryDaoJdbc(connection).findCategoryById(rs.getObject("category_id", UUID.class)).orElseThrow();
                        return Optional.of(se);
                    } else {
                        return Optional.empty();
                    }
                }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM \"spend\" WHERE username = ?"
             )) {
            ps.setString(1, username);
            ps.execute();

            try (ResultSet rs = ps.executeQuery()) {
                List<SpendEntity> spendList = new ArrayList<>();
                while (rs.next()) {
                    SpendEntity se = new SpendEntity();
                    se.setId(rs.getObject("id", UUID.class));
                    se.setUsername(rs.getString("username"));
                    se.setSpendDate(rs.getDate("spend_date"));
                    se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    se.setAmount(rs.getDouble("amount"));
                    se.setDescription(rs.getString("description"));
                    new CategoryDaoJdbc(connection).findCategoryById(rs.getObject("category_id", UUID.class)).orElseThrow();

                    spendList.add(se);
                }
                return spendList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpend(@Nonnull SpendEntity spend) {
        try (PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM spend WHERE id = ?"
             )) {
            ps.setObject(1, spend.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM spend")) {
            ps.execute();
            List<SpendEntity> spends = new ArrayList<>();

            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    SpendEntity se = new SpendEntity();
                    se.setId(rs.getObject("id", UUID.class));
                    se.setUsername(rs.getString("username"));
                    se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    se.setSpendDate(rs.getDate("spend_date"));
                    se.setAmount(rs.getDouble("amount"));
                    se.setDescription(rs.getString("description"));
                    new CategoryDaoJdbc(connection).findCategoryById(rs.getObject("category_id", UUID.class)).orElseThrow();

                    spends.add(se);
                }
            }
            return spends;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}