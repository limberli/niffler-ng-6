package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.impl.sjdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.impl.sjdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.impl.jdbc.UserdataDaoJdbc;
import guru.qa.niffler.data.impl.sjdbc.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class UserdataDbClient {

    private static final Config CFG = Config.getInstance();

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    public UserEntity create(UserEntity user) {
        return transaction(connection -> {
                    return new UserdataDaoJdbc(connection).createUser(user);
                }, CFG.userdataJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );

    }

    public Optional<UserEntity> findById(UUID id) {
        return transaction(connection -> {
                    return new UserdataDaoJdbc(connection).findById(id);
                }, CFG.userdataJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<UserEntity> findByUsername(String username) {
        return transaction(connection -> {
            return new UserdataDaoJdbc(connection).findByUsername(username);
        }, CFG.userdataJdbcUrl(), TRANSACTION_ISOLATION_LEVEL
                );
    }

    public void delete(UserEntity user) {
        transaction(connection -> {
                    new UserdataDaoJdbc(connection).delete(user);
                }, CFG.userdataJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authUser);
        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                            //setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authorityEntities);
        return UserJson.fromEntity(
                new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                        .createUser(UserEntity.fromJson(user))
        );
    }

}