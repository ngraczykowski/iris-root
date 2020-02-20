package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.UpdatedAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.BulkAnalystService;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkAddAnalystRoleRequest;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkCreateAnalystsRequest;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkCreateAnalystsRequest.NewAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkDeleteAnalystsRequest;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkUpdateDisplayNameRequest;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkUpdateDisplayNameRequest.UpdatedDisplayName;
import com.silenteight.sens.webapp.user.sync.analyst.dto.Analyst;
import com.silenteight.sens.webapp.user.sync.analyst.dto.SyncAnalystStatsDto;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class SyncAnalystsUseCase {

  private final UserListQuery userListQuery;
  private final ExternalAnalystRepository externalAnalystRepository;
  private final AnalystSynchronizer analystSynchronizer;
  private final BulkAnalystService bulkAnalystService;

  public SyncAnalystStatsDto synchronize() {
    Collection<UserDto> users = userListQuery.list();
    Collection<Analyst> analysts = externalAnalystRepository.list();
    SynchronizedAnalysts result = analystSynchronizer.synchronize(users, analysts);

    createAnalysts(result.getAdded());
    addAnalystRoles(result.getAddedRole());
    updateDisplayNames(result.getUpdatedDisplayName());
    deleteAnalysts(result.getDeleted());

    return new SyncAnalystStatsDto(
        result.addedCount(),
        result.addedRoleCount(),
        result.updatedDisplayNameCount(),
        result.deletedCount());
  }

  private void createAnalysts(List<Analyst> analysts) {
    if (!analysts.isEmpty())
      bulkAnalystService.create(createBulkCreateAnalystsRequest(analysts));
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

  private void addAnalystRoles(List<String> usernames) {
    if (!usernames.isEmpty())
      bulkAnalystService.addRole(new BulkAddAnalystRoleRequest(usernames));
  }

  private void updateDisplayNames(List<UpdatedAnalyst> analysts) {
    if (!analysts.isEmpty())
      bulkAnalystService.updateDisplayName(createBulkUpdateDisplayNameRequest(analysts));
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

  private void deleteAnalysts(List<String> usernames) {
    if (!usernames.isEmpty())
      bulkAnalystService.delete(new BulkDeleteAnalystsRequest(usernames));
  }
}
