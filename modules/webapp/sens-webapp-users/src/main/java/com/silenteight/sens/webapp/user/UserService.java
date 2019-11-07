package com.silenteight.sens.webapp.user;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.common.query.PageableResult;
import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.domain.user.UserAudit;
import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.user.dto.AddRoleRequest;
import com.silenteight.sens.webapp.user.dto.CreateUserRequest;
import com.silenteight.sens.webapp.user.dto.UpdateUserRequest;
import com.silenteight.sens.webapp.user.exception.RoleNotFoundException;
import com.silenteight.sens.webapp.user.exception.UserAlreadyExistException;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class UserService {

  @NonNull
  private final UserRepository userRepository;

  @NonNull
  private final PasswordEncoder passwordEncoder;

  public Optional<User> getUserByName(String username) {
    return userRepository.findOneByUserName(username);
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
    Optional<User> user = userRepository.findOneByUserName(userName);
    if (user.isPresent())
      throw new UserAlreadyExistException(userName);
  }

  List<User> findAll(List<Long> userIds) {
    if (userIds.isEmpty())
      return emptyList();
    return userRepository.findByUserId(userIds).collect(toList());
  }

  @Transactional
  public User update(UpdateUserRequest request) {
    User user = userRepository.getOne(request.getUserId());
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
    User user = userRepository.getOne(request.getUserId());
    user.addRole(request.getRole());
    return userRepository.save(user);
  }

  public PageableResult<User> find(Pageable pageable) {
    return userRepository.find(pageable);
  }

  public List<User> findAllOrderByUserName() {
    return userRepository.findAllOrderByUserName();
  }

  public User getOne(long userId) {
    return userRepository.getOne(userId);
  }

  public List<UserAudit> findAudited() {
    return userRepository.findAudited();
  }

  @Transactional
  public void delete(String username) {
    userRepository.delete(username);
  }

  @Transactional
  public void userLoggedIn(long userId) {
    User user = userRepository.getOne(userId);
    user.logIn();
    userRepository.save(user);
  }
}
