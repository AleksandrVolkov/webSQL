package com.java.sql.repos;

import com.java.sql.domain.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepo extends CrudRepository<Users, Long> {
}
