package guru.qa.niffler.data.impl.jdbc;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class AuthUserDaoJdbc implements AuthUserDao {

    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity create(@Nonnull AuthUserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, passwordEncoder.encode(user.getPassword()));
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<AuthUserEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM category")) {
            ps.execute();
            List<AuthUserEntity> categories = new ArrayList<>();

            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthUserEntity ae = new AuthUserEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    ae.setUsername(rs.getString("username"));
                    ae.setPassword(rs.getString("password"));
                    ae.setEnabled(rs.getBoolean("enabled"));
                    ae.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    ae.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    ae.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    categories.add(ae);
                }
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}