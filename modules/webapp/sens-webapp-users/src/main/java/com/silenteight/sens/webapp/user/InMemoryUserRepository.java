package com.silenteight.sens.webapp.user;

import lombok.NonNull;

import com.silenteight.sens.webapp.common.query.PageableResult;
import com.silenteight.sens.webapp.domain.user.TestUserUtil;
import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.domain.user.UserAudit;
import com.silenteight.sens.webapp.user.exception.UserNotFoundException;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.persistence.EntityNotFoundException;

import static java.util.stream.Collectors.toList;

public class InMemoryUserRepository implements UserRepository {

  private static final int INDEX_USER_NOT_FOUND = -1;
  private List<User> users = new ArrayList<>();
  private long currentUserId = 1L;

  @Override
  public PageableResult<User> find(Pageable pageable) {
    throw new NotImplementedException("Method find(Pageable) not implemented yet");
  }

  @Override
  public Optional<User> findOneByUserName(String userName) {
    int index = indexOf(userName);

    if (index == INDEX_USER_NOT_FOUND)
      return Optional.empty();
    else
      return Optional.of(users.get(index));
  }

  @Override
  public synchronized User save(@NonNull User user) {
    int index;
    if (user.getId() != null)
      index = indexOf(user.getId());
    else
      index = indexOf(user.getUserName());

    if (index == INDEX_USER_NOT_FOUND)
      insertUser(user);
    else
      users.set(index, user);

    return user;
  }

  private void insertUser(User user) {
    TestUserUtil.setUserId(user, currentUserId++);
    users.add(user);
  }

  private int indexOf(@NonNull String username) {
    return indexOf(u -> username.equalsIgnoreCase(u.getUserName()));
  }

  private int indexOf(long userId) {
    return indexOf(u -> u.getId() == userId);
  }

  private int indexOf(@NonNull Predicate<User> predicate) {
    for (int i = 0; i < users.size(); i++) {
      User user = users.get(i);

      if (predicate.test(user))
        return i;
    }

    return INDEX_USER_NOT_FOUND;
  }

  @Override
  public User getOne(long userId) {
    int index = indexOf(userId);

    if (index == INDEX_USER_NOT_FOUND)
      throw new EntityNotFoundException("User with ID " + userId + " not found");
    else
      return users.get(index);
  }

  @Override
  public List<User> findAllOrderByUserName() {
    return users
        .stream()
        .sorted(InMemoryUserRepository::compareUsername)
        .collect(toList());
  }

  private static int compareUsername(User u1, User u2) {
    return u1.getUserName().compareTo(u2.getUserName());
  }

  @Override
  public List<UserAudit> findAudited() {
    throw new NotImplementedException("Method findAudited() not implemented yet");
  }

  @Override
  public void delete(String username) throws UserNotFoundException {
    int index = indexOf(username);

    if (index == INDEX_USER_NOT_FOUND)
      throw new UserNotFoundException(username);
    else
      users.remove(index);
  }

  @Override
  public Stream<User> findByUserName(Iterable<String> userNames) {
    return Stream.empty();
  }

  @Override
  public Stream<User> findByUserId(Iterable<Long> userIds) {
    List<Long> ids = new ArrayList<>();
    userIds.forEach(ids::add);
    return users.stream().filter(user ->  ids.contains(user.getId()));
  }
}
