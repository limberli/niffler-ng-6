package guru.qa.niffler.data.impl.jdbc;

import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataDaoJdbc implements UserdataDao {

    private final Connection connection;

    public UserdataDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserEntity createUser(@Nonnull UserEntity user) {
        try (PreparedStatement userPs = connection.prepareStatement(
                     "INSERT INTO \"user\" (" +
                             "username, currency, firstname, surname, photo, photo_small, full_name) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setBytes(5, user.getPhoto());
            userPs.setBytes(6, user.getPhotoSmall());
            userPs.setString(7, user.getFullname());

            userPs.executeUpdate();

            UUID generatedUserId;
            try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedUserId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t access to id");
                }
            }
            user.setId(generatedUserId);
            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setId(rs.getObject("id", UUID.class));
                        userEntity.setUsername(rs.getString("username"));
                        userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        userEntity.setFirstname(rs.getString("firstname"));
                        userEntity.setSurname(rs.getString("surname"));
                        userEntity.setPhoto(rs.getBytes("photo"));
                        userEntity.setPhotoSmall(rs.getBytes("photo_small"));
                        userEntity.setFullname(rs.getString("full_name"));
                        return Optional.of(userEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE  username = ?;"
            )) {
                ps.setString(1, username);
                ps.execute();

                UserEntity userEntity = new UserEntity();
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userEntity.setId(rs.getObject("id", UUID.class));
                        userEntity.setUsername(rs.getString("username"));
                        userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        userEntity.setFirstname(rs.getString("firstname"));
                        userEntity.setSurname(rs.getString("surname"));
                        userEntity.setPhoto(rs.getBytes("photo"));
                        userEntity.setPhotoSmall(rs.getBytes("photo_small"));
                        userEntity.setFullname(rs.getString("full_name"));
                        return Optional.of(userEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@Nonnull UserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM \"user\" WHERE id = ?"
             )) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<UserEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"user\"")) {
            ps.execute();
            List<UserEntity> users = new ArrayList<>();

            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    UserEntity ue = new UserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    ue.setFirstname(rs.getString("firstname"));
                    ue.setSurname(rs.getString("surname"));
                    ue.setFullname(rs.getString("full_name"));
                    ue.setPhoto(rs.getBytes("photo"));
                    ue.setPhotoSmall(rs.getBytes("photo_small"));
                    users.add(ue);
                }
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}