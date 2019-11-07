package com.silenteight.sens.webapp.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.DecisionTreeAssignments;
import com.silenteight.sens.webapp.DecisionTreeAssignments.Assignment;
import com.silenteight.sens.webapp.common.query.PageableResult;
import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.user.dto.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class UserFinderImpl implements UserFinder {

  @NonNull
  private final UserService userService;
  @NonNull
  private final UserAuditDtoMapper auditDtoMapper;
  @NonNull
  private DecisionTreeAssignments decisionTreeAssignments;

  @Override
  public UserResponseView findUsers(@NonNull Pageable pageable) {
    PageableResult<User> result = userService.find(pageable);
    return UserResponseView
        .builder()
        .total(result.getTotal())
        .results(mapUsers(result))
        .build();
  }

  private List<UserView> mapUsers(PageableResult<User> result) {
    return result
        .getResults()
        .stream()
        .map(UserView::new)
        .collect(toList());
  }

  @Override
  public List<UserView> findAll() {
    return userService
        .findAllOrderByUserName()
        .stream()
        .map(UserView::new)
        .collect(toList());
  }

  @Override
  public List<UserAuditDto> findAudited() {
    return userService
        .findAudited()
        .stream()
        .map(auditDtoMapper::map)
        .collect(toList());
  }

  @Override
  public UserDetailedView findUser(long userId) {
    User user = userService.getOne(userId);
    List<Assignment> assignments = decisionTreeAssignments.retrieveAssignments(userId);
    return new UserDetailedView(user, assignments);
  }

  @Override
  public List<UserNameView> findUsersWithoutRole(List<Long> userIds, Role role) {
    return userService
        .findAll(userIds)
        .stream()
        .filter(user -> !user.getActiveRoles().contains(role))
        .map(UserNameView::new)
        .collect(toList());
  }
}
