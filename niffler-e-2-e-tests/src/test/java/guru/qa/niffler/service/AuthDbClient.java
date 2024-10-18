package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.impl.jdbc.AuthAuthorityDaoJdbc;

import java.sql.Connection;

import static guru.qa.niffler.data.Databases.transaction;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    public AuthUserEntity createUser(AuthUserEntity user) {
        return transaction(connection -> {
                    return new AuthAuthorityDaoJdbc(connection).create(user);
                      // Разобраться почему не работает
                },
                CFG.authJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }
}