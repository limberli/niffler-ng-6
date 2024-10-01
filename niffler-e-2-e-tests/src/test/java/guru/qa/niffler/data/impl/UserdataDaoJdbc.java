package guru.qa.niffler.data.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserdataDaoJdbc implements UserdataDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
             PreparedStatement userPs = connection.prepareStatement(
                     "INSERT INTO \"user\" (" +
                             "username, currency, firstname, surname, photo, photo_small, full_name) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setObject(5, user.getPhoto());
            userPs.setObject(6, user.getPhotoSmall());
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
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM \"user\" WHERE id = ?"
             )) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}