package com.silenteight.sens.webapp.users.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends Repository<User, Long> {

  User save(User entity);

  User getById(long userId);

  Optional<User> findOneByDeletedAtIsNullAndUserName(String userName);

  Page<User> findByDeletedAtIsNullOrderByUserNameAsc(Pageable pageable);

  List<User> findByDeletedAtIsNullOrderByUserNameAsc();

  @Modifying
  @Query("update User u set u.deletedAt=?1 where u.deletedAt is null and u.userName = ?2")
  int delete(Instant deletedAt, String username);

  @Query("SELECT u FROM User u WHERE u.id in :userIds")
  Stream<User> findById(@Param("userIds") Iterable<Long> userId);

  @Query("SELECT u FROM User u WHERE u.userName in :userNames")
  Stream<User> findByUserName(@Param("userNames") Iterable<String> userNames);
}
