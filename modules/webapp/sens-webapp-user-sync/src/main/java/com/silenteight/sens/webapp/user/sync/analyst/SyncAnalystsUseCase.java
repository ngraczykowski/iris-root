package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.UpdatedAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.BulkAnalystService;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.BulkAnalystService.Result;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.*;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkCreateAnalystsRequest.NewAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkUpdateDisplayNameRequest.UpdatedDisplayName;
import com.silenteight.sens.webapp.user.sync.analyst.dto.Analyst;
import com.silenteight.sens.webapp.user.sync.analyst.dto.SyncAnalystStatsDto;

import java.util.Collection;
import java.util.List;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class SyncAnalystsUseCase {

  private final UserListQuery userListQuery;
  private final ExternalAnalystRepository externalAnalystRepository;
  private final AnalystSynchronizer analystSynchronizer;
  private final BulkAnalystService bulkAnalystService;

  public SyncAnalystStatsDto synchronize() {
    log.debug(USER_MANAGEMENT, "Synchronizing Analysts");

    Collection<UserDto> users = userListQuery.listAll();
    Collection<Analyst> analysts = externalAnalystRepository.list();
    SynchronizedAnalysts syncResult = analystSynchronizer.synchronize(users, analysts);

    Result addedResult = createAnalysts(syncResult.getAdded());
    Result restoredResult = restoreAnalysts(syncResult.getRestored());
    Result addedRoleResult = addAnalystRoles(syncResult.getAddedRole());
    Result updatedDisplayNameResult = updateDisplayNames(syncResult.getUpdatedDisplayName());
    Result deletedResult = deleteAnalysts(syncResult.getDeleted());

    return new SyncAnalystStatsDto(
        addedResult.asMessage(),
        restoredResult.asMessage(),
        addedRoleResult.asMessage(),
        updatedDisplayNameResult.asMessage(),
        deletedResult.asMessage());
  }

  private Result createAnalysts(List<Analyst> analysts) {
    return bulkAnalystService.create(createBulkCreateAnalystsRequest(analysts));
  }

  private static BulkCreateAnalystsRequest createBulkCreateAnalystsRequest(
      List<Analyst> analysts) {

    return new BulkCreateAnalystsRequest(mapToNewAnalysts(analysts));
  }

  private static List<NewAnalyst> mapToNewAnalysts(List<Analyst> analysts) {
    return analysts
        .stream()
        .map(analyst -> new NewAnalyst(analyst.getUserName(), analyst.getDisplayName()))
        .collect(toList());
  }

  private Result restoreAnalysts(List<String> usernames) {
    return bulkAnalystService.restore(new BulkRestoreAnalystsRequest(usernames));
  }

  private Result addAnalystRoles(List<String> usernames) {
    return bulkAnalystService.addRole(new BulkAddAnalystRoleRequest(usernames));
  }

  private Result updateDisplayNames(List<UpdatedAnalyst> analysts) {
    return bulkAnalystService.updateDisplayName(createBulkUpdateDisplayNameRequest(analysts));
  }

  private static BulkUpdateDisplayNameRequest createBulkUpdateDisplayNameRequest(
      List<UpdatedAnalyst> analysts) {

    return new BulkUpdateDisplayNameRequest(mapToUpdatedDisplayName(analysts));
  }

  private static List<UpdatedDisplayName> mapToUpdatedDisplayName(List<UpdatedAnalyst> analysts) {
    return analysts
        .stream()
        .map(analyst -> new UpdatedDisplayName(analyst.getUserName(), analyst.getDisplayName()))
        .collect(toList());
  }

  private Result deleteAnalysts(List<String> usernames) {
    return bulkAnalystService.delete(new BulkDeleteAnalystsRequest(usernames));
  }
}
