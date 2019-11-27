package com.silenteight.sens.webapp.users.user;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.common.audit.AuditRevision;
import com.silenteight.sens.webapp.common.query.PageableResult;
import com.silenteight.sens.webapp.common.support.audit.AuditReaderProvider;
import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.dto.AddRoleRequest;
import com.silenteight.sens.webapp.users.user.dto.CreateUserRequest;
import com.silenteight.sens.webapp.users.user.dto.UpdateUserRequest;
import com.silenteight.sens.webapp.users.user.exception.RoleNotFoundException;
import com.silenteight.sens.webapp.users.user.exception.UserAlreadyExistException;
import com.silenteight.sens.webapp.users.user.exception.UserNotFoundException;

import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.criteria.AuditCriterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class UserService {

  @SuppressWarnings("squid:S2068")
  private static final String PASSWORD_PROPERTY = "hashedPassword";

  private static final int AUDITED_MAX_RESULTS = 10000;

  @NonNull
  private final UserRepository userRepository;

  @NonNull
  private final PasswordEncoder passwordEncoder;

  @NonNull
  private final AuditReaderProvider auditReaderProvider;

  public Optional<User> getUserByName(String username) {
    return userRepository.findOneByDeletedAtIsNullAndUserName(username);
  }

  @Transactional
  public Long create(@NonNull CreateUserRequest request) {
    validateRoles(request.getRoles());
    ensureUsernameIsUnique(request.getName());
    User user = new User(request.getName());

    if (!StringUtils.isEmpty(request.getPassword()))
      updateUserPassword(user, request.getPassword());

    if (!StringUtils.isEmpty(request.getDisplayName()))
      updateDisplayName(user, request.getDisplayName());

    updateUserRoles(user, request.getRoles());
    updateUserIsSuperUserFlag(user, request.isSuperUser());

    return userRepository.save(user).getId();
  }

  private static void validateRoles(List<Role> roles) {
    if (roles != null && roles.contains(null))
      throw new RoleNotFoundException();
  }

  private void ensureUsernameIsUnique(String userName) {
    Optional<User> user = userRepository.findOneByDeletedAtIsNullAndUserName(userName);
    if (user.isPresent())
      throw new UserAlreadyExistException(userName);
  }

  List<User> findAll(List<Long> userIds) {
    if (userIds.isEmpty())
      return emptyList();

    return userRepository.findById(userIds).collect(toList());
  }

  @Transactional
  public User update(UpdateUserRequest request) {
    User user = userRepository.getById(request.getUserId());
    request.getDisplayName().ifPresent(displayName -> updateDisplayName(user, displayName));
    request.getPassword().ifPresent(password -> updateUserPassword(user, password));
    request.getSuperUser().ifPresent(superuser -> updateUserIsSuperUserFlag(user, superuser));
    request.getActive().ifPresent(active -> updateUserIsActiveFlag(user, active));
    request.getRoles().ifPresent(roles -> updateUserRoles(user, castToListOfRoles(roles)));
    return userRepository.save(user);
  }

  private static void updateDisplayName(User user, String displayName) {
    user.setDisplayName(displayName);
  }

  private void updateUserPassword(User user, String password) {
    user.changePassword(passwordEncoder.encode(password));
  }

  private static void updateUserIsSuperUserFlag(User user, boolean isSuperUser) {
    if (isSuperUser)
      user.promoteToSuperUser();
    else
      user.relegateToUser();
  }

  private static void updateUserIsActiveFlag(User user, boolean isActive) {
    if (isActive)
      user.activate();
    else
      user.deactivate();
  }

  @SuppressWarnings("unchecked")
  private static <T extends List<Role>> T castToListOfRoles(Object obj) {
    return (T) obj;
  }

  private static void updateUserRoles(User user, List<Role> roles) {
    user.changeRoles(roles);
  }

  @Transactional
  public User addRole(AddRoleRequest request) {
    User user = userRepository.getById(request.getUserId());
    user.addRole(request.getRole());
    return userRepository.save(user);
  }

  public PageableResult<User> find(Pageable pageable) {
    Page<User> users = userRepository.findByDeletedAtIsNullOrderByUserNameAsc(pageable);
    return toPageableResult(users);
  }

  private static PageableResult<User> toPageableResult(Page<User> users) {
    return new PageableResult<>(users.getContent(), users.getTotalElements());
  }

  public List<User> findAllOrderByUserName() {
    return userRepository.findByDeletedAtIsNullOrderByUserNameAsc();
  }

  public User getOne(long userId) {
    return userRepository.getById(userId);
  }

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
        .setMaxResults(AUDITED_MAX_RESULTS)
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

  @Transactional
  public void delete(String username) {
    int countAffected = userRepository.delete(Instant.now(), username);
    if (countAffected == 0)
      throw new UserNotFoundException(username);
  }

  @Transactional
  public void userLoggedIn(long userId) {
    User user = userRepository.getById(userId);
    user.logIn();
    userRepository.save(user);
  }
}
