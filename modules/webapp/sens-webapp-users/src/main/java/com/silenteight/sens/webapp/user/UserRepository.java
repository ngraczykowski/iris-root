package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.common.adapter.audit.AuditRepository;
import com.silenteight.sens.webapp.common.query.PageableResult;
import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.domain.user.UserAudit;
import com.silenteight.sens.webapp.user.exception.UserNotFoundException;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

interface UserRepository extends AuditRepository<UserAudit> {

  PageableResult<User> find(Pageable pageable);

  Optional<User> findOneByUserName(String userName);

  User save(User user);

  User getOne(long userId);

  List<User> findAllOrderByUserName();

  void delete(String username) throws UserNotFoundException;

  Stream<User> findByUserName(Iterable<String> userNames);

  Stream<User> findByUserId(Iterable<Long> userIds);
}
