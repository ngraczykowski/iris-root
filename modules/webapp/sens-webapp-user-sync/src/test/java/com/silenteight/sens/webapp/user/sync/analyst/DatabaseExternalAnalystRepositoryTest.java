package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.sync.analyst.dto.ExternalAnalyst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static com.silenteight.sens.webapp.user.sync.analyst.ExternalAnalystFixtures.ANALYST_WITHOUT_DISPLAY_NAME;
import static com.silenteight.sens.webapp.user.sync.analyst.ExternalAnalystFixtures.ANALYST_WITH_DISPLAY_NAME;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DatabaseExternalAnalystRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  private DatabaseExternalAnalystRepository underTest;

  @BeforeEach
  void setUp() {
    underTest = new DatabaseExternalAnalystRepository("relation-name", jdbcTemplate);
  }

  @Test
  void analystsListWhenAnalystsAvailable() {
    //given
    given(jdbcTemplate.query(anyString(), any(RowMapper.class)))
        .willReturn(asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME));

    // when
    List<ExternalAnalyst> analysts = underTest.list();

    // then
    assertThat(analysts.size()).isEqualTo(2);
    assertThat(analysts).containsExactlyInAnyOrder(
        ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);
  }
}
