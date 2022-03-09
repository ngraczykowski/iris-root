package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm;

import com.silenteight.proto.serp.v1.alert.Decision;
import com.silenteight.scb.ingest.adapter.incomming.common.batch.DateConverter;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.EcmAnalystDecision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.Nonnull;

import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_FALSE_POSITIVE;
import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_TRUE_POSITIVE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EcmDecisionRowMapperTest {

  private EcmDecisionRowMapper ecmDecisionRowMapper;

  private static final String ACTION_DATE = "ACTION_DATE";
  private static final String ANALYST_DECISION = "ANALYST_DECISION";
  private static final String ANALYST_COMMENTS = "ANALYST_COMMENTS";

  private static final String ACTION_DATE_VALUE = "2019/01/28 17:20:44";
  private static final String ANALYST_COMMENTS_VALUE =
      "New Alert Correlation member linked to case.";

  @BeforeEach
  void setUp() {
    var dateConverter = new DateConverter("Asia/Taipei");
    var analystSolutionMapper = new EcmAnalystDecisionMapper(prepareAndGetEcmAnalystSolutions());

    ecmDecisionRowMapper = new EcmDecisionRowMapper(dateConverter, analystSolutionMapper);
  }

  private List<EcmAnalystDecision> prepareAndGetEcmAnalystSolutions() {
    var ecmAnalystSolution1 = new EcmAnalystDecision();
    ecmAnalystSolution1.setText("Risk Relevant");
    ecmAnalystSolution1.setSolution(ANALYST_TRUE_POSITIVE);

    var ecmAnalystSolution2 = new EcmAnalystDecision();
    ecmAnalystSolution2.setText("Risk Irrelevant - Activity in line with Business");
    ecmAnalystSolution2.setSolution(ANALYST_FALSE_POSITIVE);

    return List.of(ecmAnalystSolution1, ecmAnalystSolution2);
  }

  @ParameterizedTest
  @CsvSource({
      "Risk Relevant, 3",
      "Risk Irrelevant - Insufficitent Reason to Determine Suspicion, 4",
      "Risk Irrelevant - Activity in line with Business, 1",
      "Case Closed - Risk Irrelevant, 4" })
  void shouldReturnRiskDecision(String analystDecision, int number) throws SQLException {
    //given
    final ResultSet resultSet = createResultSet(analystDecision);

    //when
    final Decision decision = ecmDecisionRowMapper.mapRow(resultSet);

    //then
    assertThat(decision.getComment()).isEqualTo(ANALYST_COMMENTS_VALUE);
    assertThat(decision.getSolution().getNumber()).isEqualTo(number);
  }

  @Nonnull
  private static ResultSet createResultSet(String analystDecisionValue) throws SQLException {
    final ResultSet resultSet = mock(ResultSet.class);
    when(resultSet.getString(ACTION_DATE)).thenReturn(ACTION_DATE_VALUE);
    when(resultSet.getString(ANALYST_DECISION)).thenReturn(analystDecisionValue);
    when(resultSet.getString(ANALYST_COMMENTS)).thenReturn(ANALYST_COMMENTS_VALUE);
    return resultSet;
  }
}
