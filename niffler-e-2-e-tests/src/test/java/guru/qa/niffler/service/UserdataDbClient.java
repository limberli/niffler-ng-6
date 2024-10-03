package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.impl.UserdataDaoJdbc;

import java.util.Optional;
import java.util.UUID;

public class UserdataDbClient {

    private final UserdataDao userDAO = new UserdataDaoJdbc();

    public UserEntity create(UserEntity user) {
        return userDAO.createUser(user);
    }

    public Optional<UserEntity> findById(UUID id) {
        return userDAO.findById(id);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public void delete(UserEntity user) {
        userDAO.delete(user);
    }

}