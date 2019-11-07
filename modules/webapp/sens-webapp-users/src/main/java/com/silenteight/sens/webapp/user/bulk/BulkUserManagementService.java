package com.silenteight.sens.webapp.user.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.domain.user.Analyst;
import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.user.AnalystSynchronizer;
import com.silenteight.sens.webapp.user.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.user.UserService;
import com.silenteight.sens.webapp.user.bulk.dto.BulkAddRoleToUsersRequest;
import com.silenteight.sens.webapp.user.bulk.dto.BulkCreateUsersRequest;
import com.silenteight.sens.webapp.user.bulk.dto.BulkCreateUsersRequest.NewUser;
import com.silenteight.sens.webapp.user.bulk.dto.BulkDeleteUsersRequest;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_ANALYST;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class BulkUserManagementService {

  private final AnalystSynchronizer analystSynchronizer = new AnalystSynchronizer();

  @NonNull
  private final UserService userService;

  @Transactional
  public void bulkCreateUsers(BulkCreateUsersRequest request) {
    request.asCreateUserRequests().forEach(userService::create);
  }

  @Transactional
  public void bulkAddRoleToUsers(BulkAddRoleToUsersRequest request) {
    request.asAddRoleRequests().forEach(userService::addRole);
  }

  @Transactional
  public void bulkDeleteUsers(BulkDeleteUsersRequest request) {
    request.getUserNames().forEach(userService::delete);
  }

  @Transactional
  public void synchronizeAnalysts(List<Analyst> analysts) {
    List<User> users = userService.findAllOrderByUserName();
    SynchronizedAnalysts result = analystSynchronizer.synchronize(users, analysts);

    createAnalysts(result.getAdded());
    updateAnalysts(result.getUpdated());
    deleteAnalysts(result.getDeleted());
  }

  private void createAnalysts(List<Analyst> analysts) {
    if (!analysts.isEmpty())
      bulkCreateUsers(createBulkCreateUsersRequest(analysts));
  }

  private BulkCreateUsersRequest createBulkCreateUsersRequest(List<Analyst> analysts) {
    return new BulkCreateUsersRequest(mapToNewUsers(analysts));
  }

  private List<NewUser> mapToNewUsers(List<Analyst> analysts) {
    return analysts
        .stream()
        .map(analyst -> new NewUser(analyst.getLogin(), ROLE_ANALYST, analyst.getDisplayName()))
        .collect(toList());
  }

  private void updateAnalysts(List<Long> analystIds) {
    if (!analystIds.isEmpty())
      bulkAddRoleToUsers(new BulkAddRoleToUsersRequest(analystIds, ROLE_ANALYST));
  }

  private void deleteAnalysts(List<String> analysts) {
    if (!analysts.isEmpty())
      bulkDeleteUsers(new BulkDeleteUsersRequest(analysts));
  }
}
