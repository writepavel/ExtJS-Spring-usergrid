package com.writepavel.usergrid.persistence.repository;

import com.writepavel.usergrid.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Pavel on 03.08.2014.
 */

@Repository
public interface UsersRepository extends CrudRepository<User, String>
{
    void delete(String key);

    User findOne(String key);

    Iterable<User> findAll();

    User save(User user);
}
