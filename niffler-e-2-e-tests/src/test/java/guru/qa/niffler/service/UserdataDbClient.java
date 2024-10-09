package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.impl.UserdataDaoJdbc;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class UserdataDbClient {

    private static final Config CFG = Config.getInstance();

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

}