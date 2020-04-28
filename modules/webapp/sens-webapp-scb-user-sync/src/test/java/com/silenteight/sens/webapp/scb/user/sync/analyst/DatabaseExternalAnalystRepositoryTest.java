package com.silenteight.sens.webapp.scb.user.sync.analyst;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.Analyst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;

import static com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystFixtures.ANALYST_WITHOUT_DISPLAY_NAME;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystFixtures.ANALYST_WITH_DISPLAY_NAME;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DatabaseExternalAnalystRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @Mock
  private AuditLog auditLog;

  private DatabaseExternalAnalystRepository underTest;

  @BeforeEach
  void setUp() {
    underTest = new DatabaseExternalAnalystRepository("relation-name", jdbcTemplate, auditLog);
  }

  @Test
  void analystsListWhenAnalystsAvailable() {
    //given
    given(jdbcTemplate.query(anyString(), any(RowMapper.class)))
        .willReturn(asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME));

    // when
    Collection<Analyst> analysts = underTest.list();

    // then
    assertThat(analysts.size()).isEqualTo(2);
    assertThat(analysts).containsExactlyInAnyOrder(
        ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);
  }
}
