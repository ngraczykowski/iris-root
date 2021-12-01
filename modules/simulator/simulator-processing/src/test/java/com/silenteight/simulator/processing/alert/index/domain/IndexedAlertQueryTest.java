package com.silenteight.simulator.processing.alert.index.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.processing.alert.index.domain.exception.IndexedAlertEntityNotFoundException;
import com.silenteight.simulator.processing.alert.index.dto.IndexedAlertDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.simulator.processing.alert.index.domain.IndexedAlertFixtures.*;
import static com.silenteight.simulator.processing.alert.index.domain.State.ACKED;
import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;
import static java.util.List.of;
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
  void shouldReturn1WhenThereIsAckedIndexedAlert() {
    // given
    repository.save(ACKED_INDEXED_ALERT_ENTITY);
    repository.save(SENT_INDEXED_ALERT_ENTITY);

    // when
    long result = underTest.count(ANALYSIS_NAME, of(ACKED));

    // then
    assertThat(result).isEqualTo(1L);
  }

  @Test
  void shouldReturn0WhenThereIsNoAckedIndexedAlerts() {
    // given
    repository.save(SENT_INDEXED_ALERT_ENTITY);

    // when
    long result = underTest.count(ANALYSIS_NAME, of(ACKED));

    // then
    assertThat(result).isZero();
  }

  @ParameterizedTest
  @MethodSource("getAlertsCountCriteria")
  void shouldReturnAlertsCount(List<State> states, long expectedResult) {
    // given
    repository.save(ACKED_INDEXED_ALERT_ENTITY);
    repository.save(SENT_INDEXED_ALERT_ENTITY);

    // when
    long result = underTest.sumAllAlertsCountWithAnalysisName(ANALYSIS_NAME, states);

    // then
    assertThat(result).isEqualTo(expectedResult);
  }

  private static Stream<Arguments> getAlertsCountCriteria() {
    return Stream.of(
        Arguments.of(of(ACKED), 5),
        Arguments.of(of(SENT), 5),
        Arguments.of(of(ACKED, SENT), 10));
  }
}
