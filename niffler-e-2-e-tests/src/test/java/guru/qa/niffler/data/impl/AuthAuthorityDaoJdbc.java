package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.Authority;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setReadAndWriteAuthorityToUser(UUID userId) {
        try (PreparedStatement psRead = connection.prepareStatement("INSERT INTO authority (user_id, authority) VALUES (?, ?)");
             PreparedStatement psWrite = connection.prepareStatement("INSERT INTO authority (user_id, authority) VALUES (?, ?)")) {
            psRead.setObject(1, userId);
            psRead.setString(2, Authority.read.name());
            psRead.executeUpdate();

            psWrite.setObject(1, userId);
            psWrite.setString(2, Authority.write.name());
            psWrite.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}