package com.silenteight.sens.webapp.scb.user.sync.analyst;

import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.BulkAnalystService;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.BulkResult;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.SingleResult;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.*;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.BulkCreateAnalystsRequest.NewAnalyst;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.BulkUpdateDisplayNameRequest.UpdatedDisplayName;
import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.SyncAnalystStatsDto;
import com.silenteight.sens.webapp.user.UserListQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystFixtures.ANALYST_WITHOUT_DISPLAY_NAME;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystFixtures.ANALYST_WITH_DISPLAY_NAME;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystFixtures.NEW_ANALYST;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystFixtures.RESTORED_ANALYST;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.UserDtoFixtures.*;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.SingleResult.failure;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.SingleResult.success;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.LongStream.range;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncAnalystsUseCaseTest {

  @Mock
  private UserListQuery userListQuery;

  @Mock
  private ExternalAnalystRepository externalAnalystRepository;

  @Mock
  private BulkAnalystService bulkAnalystService;

  private SyncAnalystProperties syncAnalystProperties;

  private SyncAnalystsUseCase underTest;

  @BeforeEach
  void setUp() {
    syncAnalystProperties = new SyncAnalystProperties();
    underTest = new SyncAnalystConfiguration()
        .syncAnalystsUseCase(
            userListQuery, externalAnalystRepository, bulkAnalystService, syncAnalystProperties);
  }

  @Test
  void usersAvailable_syncAnalysts() {
    // given
    when(userListQuery.listAll()).thenReturn(
        asList(
            SENS_USER,
            GNS_USER_WITHOUT_ANALYST_ROLE,
            GNS_USER_WITHOUT_DISPLAY_NAME,
            GNS_USER,
            DELETED_GNS_USER));
    when(externalAnalystRepository.list()).thenReturn(emptyList());
    when(bulkAnalystService.create(any(BulkCreateAnalystsRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.restore(any(BulkRestoreAnalystsRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.addRole(any(BulkAddAnalystRoleRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.updateDisplayName(any(BulkUpdateDisplayNameRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.delete(any(BulkDeleteAnalystsRequest.class)))
        .thenReturn(createResult(2));

    // when
    SyncAnalystStatsDto stats = underTest.synchronize();

    // then
    assertThat(stats.getAdded()).isEqualTo("0 / 0");
    assertThat(stats.getRestored()).isEqualTo("0 / 0");
    assertThat(stats.getAddedRole()).isEqualTo("0 / 0");
    assertThat(stats.getUpdatedDisplayName()).isEqualTo("0 / 0");
    assertThat(stats.getDeleted()).isEqualTo("2 / 2");
    verify(bulkAnalystService).create(new BulkCreateAnalystsRequest(emptyList()));
    verify(bulkAnalystService).restore(new BulkRestoreAnalystsRequest(emptyList()));
    verify(bulkAnalystService).addRole(new BulkAddAnalystRoleRequest(emptyList()));
    verify(bulkAnalystService).updateDisplayName(new BulkUpdateDisplayNameRequest(emptyList()));
    verify(bulkAnalystService).delete(
        new BulkDeleteAnalystsRequest(
            asList(
                GNS_USER_WITHOUT_DISPLAY_NAME.getUserName(),
                GNS_USER.getUserName())));
  }

  @Test
  void analystsAvailable_syncAnalysts() {
    // given
    when(userListQuery.listAll()).thenReturn(emptyList());
    when(externalAnalystRepository.list()).thenReturn(
        asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME, NEW_ANALYST));
    when(bulkAnalystService.create(any(BulkCreateAnalystsRequest.class)))
        .thenReturn(createResult(3));
    when(bulkAnalystService.restore(any(BulkRestoreAnalystsRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.addRole(any(BulkAddAnalystRoleRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.updateDisplayName(any(BulkUpdateDisplayNameRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.delete(any(BulkDeleteAnalystsRequest.class)))
        .thenReturn(createResult(0));

    // when
    SyncAnalystStatsDto stats = underTest.synchronize();

    // then
    assertThat(stats.getAdded()).isEqualTo("3 / 3");
    assertThat(stats.getRestored()).isEqualTo("0 / 0");
    assertThat(stats.getAddedRole()).isEqualTo("0 / 0");
    assertThat(stats.getUpdatedDisplayName()).isEqualTo("0 / 0");
    assertThat(stats.getDeleted()).isEqualTo("0 / 0");
    verify(bulkAnalystService).create(
        new BulkCreateAnalystsRequest(
            asList(
                new NewAnalyst(
                    ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
                    ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName()),
                new NewAnalyst(
                    ANALYST_WITH_DISPLAY_NAME.getUserName(),
                    ANALYST_WITH_DISPLAY_NAME.getDisplayName()),
                new NewAnalyst(NEW_ANALYST.getUserName(), NEW_ANALYST.getDisplayName()))));
    verify(bulkAnalystService).restore(new BulkRestoreAnalystsRequest(emptyList()));
    verify(bulkAnalystService).addRole(new BulkAddAnalystRoleRequest(emptyList()));
    verify(bulkAnalystService).updateDisplayName(new BulkUpdateDisplayNameRequest(emptyList()));
    verify(bulkAnalystService).delete(new BulkDeleteAnalystsRequest(emptyList()));
  }

  @Test
  void usersAndAnalystsAvailable_syncAnalysts() {
    // given
    when(userListQuery.listAll()).thenReturn(
        asList(
            SENS_USER,
            GNS_USER_WITHOUT_ANALYST_ROLE,
            GNS_USER_WITHOUT_DISPLAY_NAME,
            GNS_USER,
            DELETED_GNS_USER));
    when(externalAnalystRepository.list()).thenReturn(
        asList(
            ANALYST_WITHOUT_DISPLAY_NAME,
            ANALYST_WITH_DISPLAY_NAME,
            NEW_ANALYST,
            RESTORED_ANALYST));
    when(bulkAnalystService.create(any(BulkCreateAnalystsRequest.class)))
        .thenReturn(createResult(1));
    when(bulkAnalystService.restore(any(BulkRestoreAnalystsRequest.class)))
        .thenReturn(createResult(1));
    when(bulkAnalystService.addRole(any(BulkAddAnalystRoleRequest.class)))
        .thenReturn(createResult(1));
    when(bulkAnalystService.updateDisplayName(any(BulkUpdateDisplayNameRequest.class)))
        .thenReturn(createResult(1));
    when(bulkAnalystService.delete(any(BulkDeleteAnalystsRequest.class)))
        .thenReturn(createResult(1));

    // when
    SyncAnalystStatsDto stats = underTest.synchronize();

    // then
    assertThat(stats.getAdded()).isEqualTo("1 / 1");
    assertThat(stats.getRestored()).isEqualTo("1 / 1");
    assertThat(stats.getAddedRole()).isEqualTo("1 / 1");
    assertThat(stats.getUpdatedDisplayName()).isEqualTo("1 / 1");
    assertThat(stats.getDeleted()).isEqualTo("1 / 1");
    verify(bulkAnalystService).create(
        new BulkCreateAnalystsRequest(
            singletonList(
                new NewAnalyst(NEW_ANALYST.getUserName(), NEW_ANALYST.getDisplayName()))));
    verify(bulkAnalystService).restore(
        new BulkRestoreAnalystsRequest(singletonList(RESTORED_ANALYST.getUserName())));
    verify(bulkAnalystService).addRole(
        new BulkAddAnalystRoleRequest(
            singletonList(GNS_USER_WITHOUT_ANALYST_ROLE.getUserName())));
    verify(bulkAnalystService).updateDisplayName(
        new BulkUpdateDisplayNameRequest(
            singletonList(
                new UpdatedDisplayName(
                    ANALYST_WITH_DISPLAY_NAME.getUserName(),
                    ANALYST_WITH_DISPLAY_NAME.getDisplayName()))));
    verify(bulkAnalystService).delete(
        new BulkDeleteAnalystsRequest(singletonList(GNS_USER.getUserName())));
  }

  @Test
  void analystsCreationFailed_syncAnalysts() {
    // given
    setUpDefaultNonCreationSyncResults();
    String msg1 = "msg1";
    String msg2 = "msg2";
    String msg3 = "msg3";
    when(bulkAnalystService.create(any(BulkCreateAnalystsRequest.class)))
        .thenReturn(
            bulkResult(failure(msg1), success(), failure(msg2), failure(msg3), failure("msg4")));

    syncAnalystProperties.setMaxErrors(3);
    underTest = new SyncAnalystConfiguration()
        .syncAnalystsUseCase(
            userListQuery, externalAnalystRepository, bulkAnalystService, syncAnalystProperties);

    // when
    SyncAnalystStatsDto stats = underTest.synchronize();

    // then
    assertThat(stats.getAdded()).isEqualTo("1 / 5");
    assertThat(stats.getErrors()).containsExactly(msg1, msg2, msg3);
  }

  private void setUpDefaultNonCreationSyncResults() {
    when(bulkAnalystService.restore(any(BulkRestoreAnalystsRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.addRole(any(BulkAddAnalystRoleRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.updateDisplayName(any(BulkUpdateDisplayNameRequest.class)))
        .thenReturn(createResult(0));
    when(bulkAnalystService.delete(any(BulkDeleteAnalystsRequest.class)))
        .thenReturn(createResult(0));
  }

  private static BulkResult bulkResult(SingleResult... results) {
    return new BulkResult(asList(results));
  }

  private static BulkResult createResult(long success) {
    return new BulkResult(range(0, success).mapToObj(i -> success()).collect(toList()));
  }
}
