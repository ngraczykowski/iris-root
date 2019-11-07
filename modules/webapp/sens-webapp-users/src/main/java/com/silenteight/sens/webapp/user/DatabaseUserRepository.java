package com.silenteight.sens.webapp.user;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.adapter.user.JpaUserRepository;
import com.silenteight.sens.webapp.common.entity.AuditRevision;
import com.silenteight.sens.webapp.common.query.PageableResult;
import com.silenteight.sens.webapp.common.support.audit.AuditReaderProvider;
import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.domain.user.UserAudit;
import com.silenteight.sens.webapp.user.exception.UserNotFoundException;

import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.criteria.AuditCriterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class DatabaseUserRepository implements UserRepository {

  @SuppressWarnings("squid:S2068")
  private static final String PASSWORD_PROPERTY = "hashedPassword";

  private final JpaUserRepository userRepository;
  private final AuditReaderProvider auditReaderProvider;

  @Override
  public PageableResult<User> find(Pageable pageable) {
    Page<User> users = userRepository.findByDeletedAtIsNullOrderByUserNameAsc(pageable);
    return toPageableResult(users);
  }

  private static PageableResult<User> toPageableResult(Page<User> users) {
    return new PageableResult<>(users.getContent(), users.getTotalElements());
  }

  @Override
  public Optional<User> findOneByUserName(String userName) {
    return userRepository.findOneByDeletedAtIsNullAndUserName(userName);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public User getOne(long userId) {
    return userRepository.getOne(userId);
  }

  @Override
  public List<User> findAllOrderByUserName() {
    return userRepository.findByDeletedAtIsNullOrderByUserNameAsc();
  }

  @Override
  public List<UserAudit> findAudited() {
    List<UserAudit> passwordChangedAuditedUsers = findAuditedWithChangedPassword();
    List<UserAudit> passwordNotChangedAuditedUsers = findAuditedWithoutChangedPassword();

    return Stream
        .of(passwordChangedAuditedUsers, passwordNotChangedAuditedUsers)
        .flatMap(Collection::stream)
        .sorted((a1, a2) -> a2.getAuditRevision().getId() > a1.getAuditRevision().getId() ? 1 : -1)
        .collect(toList());
  }

  private List<UserAudit> findAuditedWithChangedPassword() {
    List<Object> auditedUsers = findAudited(AuditEntity.property(PASSWORD_PROPERTY).hasChanged());
    return convertToUserAudits(auditedUsers, true);
  }

  private List<UserAudit> findAuditedWithoutChangedPassword() {
    List<Object> auditedUsers = findAudited(
        AuditEntity.property(PASSWORD_PROPERTY).hasNotChanged());
    return convertToUserAudits(auditedUsers, false);
  }

  private List<Object> findAudited(AuditCriterion criterion) {
    return auditReaderProvider
        .get()
        .createQuery()
        .forRevisionsOfEntity(User.class, false, true)
        .add(criterion)
        .setMaxResults(getAuditedMaxResults())
        .addOrder(AuditEntity.revisionNumber().desc())
        .getResultList();
  }

  private static List<UserAudit> convertToUserAudits(
      List<Object> auditedUsers, boolean passwordChanged) {
    return auditedUsers
        .stream()
        .filter(a -> a.getClass().isArray())
        .map(a -> getUserAuditData((Object[]) a, passwordChanged))
        .collect(toList());
  }

  private static UserAudit getUserAuditData(Object[] auditData, boolean passwordChanged) {
    return UserAudit
        .builder()
        .user((User) auditData[0])
        .auditRevision((AuditRevision) auditData[1])
        .revisionType((RevisionType) auditData[2])
        .passwordChanged(passwordChanged)
        .build();
  }

  @Override
  public void delete(String username) {
    int countAffected = userRepository.delete(Instant.now(), username);
    if (countAffected == 0)
      throw new UserNotFoundException(username);
  }

  @Override
  public Stream<User> findByUserName(Iterable<String> userNames) {
    return userRepository.findByUserName(userNames);
  }

  @Override
  public Stream<User> findByUserId(Iterable<Long> userIds) {
    return userRepository.findById(userIds);
  }
}
