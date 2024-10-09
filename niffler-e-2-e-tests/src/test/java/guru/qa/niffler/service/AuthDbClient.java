package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.impl.AuthUserDaoJdbc;

import java.sql.Connection;

import static guru.qa.niffler.data.Databases.transaction;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    public AuthUserEntity createUserInAuthWithAuthorities(AuthUserEntity user) {
        return transaction(connection -> {

                  AuthUserEntity created = new AuthUserDaoJdbc(connection).createUserInAuth(user);
                  new AuthAuthorityDaoJdbc(connection).setReadAndWriteAuthorityToUser(created.getId());
                    return created;
                },
                CFG.authJdbcUrl()
                ,TRANSACTION_ISOLATION_LEVEL
        );
    }
}