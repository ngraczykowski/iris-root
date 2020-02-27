package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.BulkAnalystService;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.*;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkCreateAnalystsRequest.NewAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.BulkUpdateDisplayNameRequest.UpdatedDisplayName;
import com.silenteight.sens.webapp.user.sync.analyst.dto.SyncAnalystStatsDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.user.sync.analyst.AnalystFixtures.ANALYST_WITHOUT_DISPLAY_NAME;
import static com.silenteight.sens.webapp.user.sync.analyst.AnalystFixtures.ANALYST_WITH_DISPLAY_NAME;
import static com.silenteight.sens.webapp.user.sync.analyst.AnalystFixtures.NEW_ANALYST;
import static com.silenteight.sens.webapp.user.sync.analyst.AnalystFixtures.RESTORED_ANALYST;
import static com.silenteight.sens.webapp.user.sync.analyst.UserDtoFixtures.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
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

  private SyncAnalystsUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new SyncAnalystConfiguration()
        .syncAnalystsUseCase(userListQuery, externalAnalystRepository, bulkAnalystService);
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

    // when
    SyncAnalystStatsDto stats = underTest.synchronize();

    // then
    assertThat(stats.getAdded()).isEqualTo(0);
    assertThat(stats.getRestored()).isEqualTo(0);
    assertThat(stats.getAddedRole()).isEqualTo(0);
    assertThat(stats.getUpdatedDisplayName()).isEqualTo(0);
    assertThat(stats.getDeleted()).isEqualTo(2);
    verify(bulkAnalystService, never()).create(any());
    verify(bulkAnalystService, never()).restore(any());
    verify(bulkAnalystService, never()).addRole(any());
    verify(bulkAnalystService, never()).updateDisplayName(any());
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

    // when
    SyncAnalystStatsDto stats = underTest.synchronize();

    // then
    assertThat(stats.getAdded()).isEqualTo(3);
    assertThat(stats.getRestored()).isEqualTo(0);
    assertThat(stats.getAddedRole()).isEqualTo(0);
    assertThat(stats.getUpdatedDisplayName()).isEqualTo(0);
    assertThat(stats.getDeleted()).isEqualTo(0);
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
    verify(bulkAnalystService, never()).restore(any());
    verify(bulkAnalystService, never()).addRole(any());
    verify(bulkAnalystService, never()).updateDisplayName(any());
    verify(bulkAnalystService, never()).delete(any());
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

    // when
    SyncAnalystStatsDto stats = underTest.synchronize();

    // then
    assertThat(stats.getAdded()).isEqualTo(1);
    assertThat(stats.getRestored()).isEqualTo(1);
    assertThat(stats.getAddedRole()).isEqualTo(1);
    assertThat(stats.getUpdatedDisplayName()).isEqualTo(1);
    assertThat(stats.getDeleted()).isEqualTo(1);
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
}
