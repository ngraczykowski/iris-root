package com.silenteight.auditing.bs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.OffsetDateTime;
import java.util.Collection;

import static com.silenteight.auditing.bs.AuditDataDtoFixtures.DECISION_TREE_ADD_AUDIT_DATA;
import static com.silenteight.auditing.bs.AuditDataDtoFixtures.REASONING_BRANCH_CHANGE_AUDIT_DATA;
import static java.time.OffsetDateTime.parse;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditingFinderTest {

  private static final OffsetDateTime FROM = parse("2020-04-05T10:15:30Z");
  private static final OffsetDateTime TO = parse("2020-04-28T10:15:30Z");

  @Mock
  private NamedParameterJdbcTemplate jdbcTemplate;

  private AuditingFinder underTest;

  @BeforeEach
  void setUp() {
    underTest = new AuditingFinder(jdbcTemplate);
  }

  @Test
  void noLogsAvailable_returnEmptyList() {
    // given
    when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenReturn(emptyList());

    // when
    Collection<AuditDataDto> data = underTest.find(FROM, TO);

    // then
    assertThat(data).isEmpty();
  }

  @Test
  void logsAvailable_returnAuditData() {
    // given
    when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenReturn(asList(DECISION_TREE_ADD_AUDIT_DATA, REASONING_BRANCH_CHANGE_AUDIT_DATA));

    // when
    Collection<AuditDataDto> data = underTest.find(FROM, TO);

    // then
    assertThat(data).containsExactly(
        DECISION_TREE_ADD_AUDIT_DATA, REASONING_BRANCH_CHANGE_AUDIT_DATA);
  }

  @Test
  void logsAvailable_returnAuditDataByTypes() {
    // given
    when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenReturn(asList(DECISION_TREE_ADD_AUDIT_DATA, REASONING_BRANCH_CHANGE_AUDIT_DATA));

    // when
    Collection<AuditDataDto> data = underTest.find(
        FROM,
        TO,
        asList(
            DECISION_TREE_ADD_AUDIT_DATA.getType(), REASONING_BRANCH_CHANGE_AUDIT_DATA.getType()));

    // then
    assertThat(data).containsExactly(
        DECISION_TREE_ADD_AUDIT_DATA, REASONING_BRANCH_CHANGE_AUDIT_DATA);
  }
}
