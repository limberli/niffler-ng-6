package guru.qa.niffler.data.dao;

import java.util.UUID;

public interface AuthAuthorityDao {

    void setReadAndWriteAuthorityToUser(UUID userId);

}