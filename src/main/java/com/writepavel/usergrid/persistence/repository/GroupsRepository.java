package com.writepavel.usergrid.persistence.repository;

import com.writepavel.usergrid.domain.UserGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Pavel on 03.08.2014.
 */

@Repository
public interface GroupsRepository extends CrudRepository<UserGroup, String> {

    void delete(String key);

    long count();

    UserGroup findOne(String key);

    Iterable<UserGroup> findAll();

    UserGroup save(UserGroup group);

    <S extends UserGroup> List<S> save(Iterable<S> entities);

}
