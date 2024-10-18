package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.*;
import guru.qa.niffler.data.impl.sjdbc.*;
import org.junit.jupiter.api.Test;

public class JdbcTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void springJdbcTest() {
        SpendDao spendDao = new SpendDaoSpringJdbc(Databases.dataSource(CFG.spendJdbcUrl()));
        CategoryDao categoryDao = new CategoryDaoSpringJdbc(Databases.dataSource(CFG.spendJdbcUrl()));
        AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc(Databases.dataSource(CFG.authJdbcUrl()));
        AuthUserDao authUserDao = new AuthUserDaoSpringJdbc(Databases.dataSource(CFG.authJdbcUrl()));
        UserdataDao udUserDao = new UserdataUserDaoSpringJdbc(Databases.dataSource(CFG.userdataJdbcUrl()));

        udUserDao.findAll().forEach(System.out::println);

    }
}
