package com.silenteight.sens.webapp.scb.user.sync.analyst;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystSynchronizer.UpdatedAnalyst;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.BulkAnalystService;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.BulkResult;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.*;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.BulkCreateAnalystsRequest.NewAnalyst;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.BulkUpdateDisplayNameRequest.UpdatedDisplayName;
import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.Analyst;
import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.SyncAnalystStatsDto;
import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sens.webapp.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class SyncAnalystsUseCase {

  @NonNull
  private final UserListQuery userListQuery;
  @NonNull
  private final ExternalAnalystRepository externalAnalystRepository;
  @NonNull
  private final AnalystSynchronizer analystSynchronizer;
  @NonNull
  private final BulkAnalystService bulkAnalystService;
  @NonNull
  private final AuditTracer auditTracer;
  private final int maxErrors;

  public SyncAnalystStatsDto synchronize() {
    log.info(USER_MANAGEMENT, "Synchronizing Analysts");

    auditTracer.save(new AnalystsSyncRequestedEvent());

    Collection<UserDto> users = userListQuery.listAll();
    Collection<Analyst> analysts = externalAnalystRepository.list();
    SynchronizedAnalysts syncResult = analystSynchronizer.synchronize(users, analysts);

    BulkResult addedResult = createAnalysts(syncResult.getAdded());
    BulkResult restoredResult = restoreAnalysts(syncResult.getRestored());
    BulkResult addedRoleResult = addAnalystRoles(syncResult.getAddedRole());
    BulkResult updatedDisplayNameResult = updateDisplayNames(syncResult.getUpdatedDisplayName());
    BulkResult deletedResult = deleteAnalysts(syncResult.getDeleted());

    return new SyncAnalystStatsDto(
        addedResult.asMessage(),
        restoredResult.asMessage(),
        addedRoleResult.asMessage(),
        updatedDisplayNameResult.asMessage(),
        deletedResult.asMessage(),
        addedResult.errorMessagesWithMaxSizeOf(maxErrors));
  }

  private BulkResult createAnalysts(List<Analyst> analysts) {
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

  private BulkResult restoreAnalysts(List<String> usernames) {
    return bulkAnalystService.restore(new BulkRestoreAnalystsRequest(usernames));
  }

  private BulkResult addAnalystRoles(List<String> usernames) {
    return bulkAnalystService.addRole(new BulkAddAnalystRoleRequest(usernames));
  }

  private BulkResult updateDisplayNames(List<UpdatedAnalyst> analysts) {
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

  private BulkResult deleteAnalysts(List<String> usernames) {
    return bulkAnalystService.delete(new BulkDeleteAnalystsRequest(usernames));
  }
}
