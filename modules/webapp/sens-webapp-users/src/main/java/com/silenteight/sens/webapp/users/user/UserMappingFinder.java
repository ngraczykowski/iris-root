package com.silenteight.sens.webapp.users.user;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
public class UserMappingFinder {

  @NonNull
  private final UserRepository userRepository;

  @Transactional
  public Map<String, Long> mapUserNameToUserId(Collection<String> userNames) {
    if (userNames.isEmpty())
      return emptyMap();

    Map<String, Long> mappedUserNames = getMappedUserNames(userNames);
    checkSize(mappedUserNames, userNames);
    return mappedUserNames;
  }

  private Map<String, Long> getMappedUserNames(Collection<String> userNames) {
    try (Stream<User> stream = userRepository.findByUserName(userNames)) {
      return stream
          .filter(user -> !user.isDeleted())
          .collect(toMap(User::getUserName, User::getId));
    }
  }

  @Transactional
  public Map<Long, String> mapUserIdToUserName(Collection<Long> userIds) {
    if (userIds.isEmpty())
      return emptyMap();

    Map<Long, String> mappedUserIds = getMappedUserIds(userIds);
    checkSize(mappedUserIds, userIds);
    return mappedUserIds;
  }

  private Map<Long, String> getMappedUserIds(Collection<Long> userIds) {
    try (Stream<User> stream = userRepository.findById(userIds)) {
      return stream.collect(toMap(User::getId, User::getUserName));
    }
  }

  private static <K, V> void checkSize(Map<K, V> result, Collection<K> argument) {
    Collection<K> difference = CollectionUtils.subtract(argument, result.keySet());
    if (!difference.isEmpty())
      throw new InvalidMappingResultCountException(difference);
  }

  public static class InvalidMappingResultCountException extends RuntimeException {

    private static final long serialVersionUID = 2184765622241024351L;
    private final List<String> difference;

    <K> InvalidMappingResultCountException(Collection<K> difference) {
      super("Invalid User mapping: " + difference);
      this.difference = difference.stream().map(Objects::toString).collect(toList());
    }

    public List<String> getDifference() {
      return difference;
    }
  }
}
