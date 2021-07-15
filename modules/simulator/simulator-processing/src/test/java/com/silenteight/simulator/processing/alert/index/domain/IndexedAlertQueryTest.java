package com.silenteight.simulator.processing.alert.index.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.processing.alert.index.domain.exception.IndexedAlertEntityNotFoundException;
import com.silenteight.simulator.processing.alert.index.dto.IndexedAlertDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.silenteight.simulator.processing.alert.index.domain.IndexedAlertFixtures.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { IndexedAlertTestConfiguration.class })
class IndexedAlertQueryTest extends BaseDataJpaTest {

  private static final String INCORRECT_REQUEST_ID = "777";

  @Autowired
  IndexedAlertRepository repository;

  @Autowired
  IndexedAlertQuery underTest;

  @Test
  void shouldFindTwoByAnalysisName() {
    // given
    repository.save(SENT_INDEXED_ALERT_ENTITY);
    repository.save(ACKED_INDEXED_ALERT_ENTITY);

    // when
    List<IndexedAlertDto> indexedAlertDtos = underTest.findAllByAnalysisName(ANALYSIS_NAME);

    // then
    assertThat(indexedAlertDtos).hasSize(2);
  }

  @Test
  void shouldGetAnalysisNameByRequestId() {
    //given
    repository.save(SENT_INDEXED_ALERT_ENTITY);

    // when
    String analysisName = underTest.getAnalysisNameByRequestId(REQUEST_ID);

    // then
    assertThat(analysisName).isEqualTo(ANALYSIS_NAME);
  }

  @Test
  void shouldGetIndexedAlertEntityByRequestId() {
    // given
    repository.save(ACKED_INDEXED_ALERT_ENTITY);

    // when
    IndexedAlertEntity entity = underTest.getIndexedAlertEntityByRequestId(REQUEST_ID_2);

    // then
    assertThat(entity.getRequestId()).isEqualTo(REQUEST_ID_2);
  }

  @Test
  void shouldThrowExceptionWhenCannotGetAnalysisNameByRequestId() {
    assertThatThrownBy(() -> underTest.getAnalysisNameByRequestId(INCORRECT_REQUEST_ID))
        .isInstanceOf(IndexedAlertEntityNotFoundException.class)
        .hasMessageContaining("requestId=" + INCORRECT_REQUEST_ID);
  }

  @Test
  void shouldThrowExceptionWhenCannotGetIndexedAlertEntityByRequestId() {
    assertThatThrownBy(() -> underTest.getIndexedAlertEntityByRequestId(INCORRECT_REQUEST_ID))
        .isInstanceOf(IndexedAlertEntityNotFoundException.class)
        .hasMessageContaining("requestId=" + INCORRECT_REQUEST_ID);
  }

  @Test
  void shouldReturnTrueWhenAllIndexedAlertsAreAcked() {
    // given
    repository.save(ACKED_INDEXED_ALERT_ENTITY);

    // when
    boolean result = underTest.areAllIndexedAlertsAcked(ANALYSIS_NAME);

    // then
    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnFalseWhenSomeIndexedAlertsAreNotAcked() {
    // given
    repository.save(ACKED_INDEXED_ALERT_ENTITY);
    repository.save(SENT_INDEXED_ALERT_ENTITY);

    // when
    boolean result = underTest.areAllIndexedAlertsAcked(ANALYSIS_NAME);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void shouldReturn5AlertsCount() {
    // given
    repository.save(ACKED_INDEXED_ALERT_ENTITY);

    // when
    long result = underTest.sumAllAlertsCountWithAnalysisName(ANALYSIS_NAME);

    // then
    assertThat(result).isEqualTo(5L);
  }
}