package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;

import java.util.ArrayList;
import java.util.List;

import static com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFixture.createInsertRequest;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcRecommendationDataAccess.class,
    JdbcRecommendationConfiguration.class
})
@Sql(scripts = {
    "classpath:fixtures/03_Analysis.sql",
    "classpath:fixtures/02_Dataset.sql",
})
@SqlMergeMode(MergeMode.MERGE)
class JdbcRecommendationDataAccessIT extends BaseJdbcTest {

  @Autowired
  JdbcRecommendationDataAccess recommendationDataAccess;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Test
  @Sql
  void shouldSelectAllReadyRecommendations() {
    var result = recommendationDataAccess.selectPendingAlerts(1);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = { "JdbcRecommendationDataAccessIT.context.sql" })
  void shouldSelectAlertsContextFromAnalysis() {
    var alerts = new ArrayList<AlertRecommendation>();
    recommendationDataAccess.streamAlertRecommendations(1, alerts::add);
    assertThat(alerts.size()).isEqualTo(2);
    assertContextData(alerts);
  }

  @Test
  @Sql(scripts = { "JdbcRecommendationDataAccessIT.context.sql" })
  void shouldSelectAlertsContextFromAnalysisAndDataset() {
    var alerts = new ArrayList<AlertRecommendation>();
    recommendationDataAccess.streamAlertRecommendations(1, 1, alerts::add);
    assertThat(alerts.size()).isEqualTo(1);
    assertContextData(alerts);
  }

  /**
   * When a single alert is in two analysis, the ae_alert_match_solutions_query view returns
   * duplicated match_ids, which in turn prevents from showing the alert as pending due to the JOIN
   * condition.
   */
  @Test
  @Sql
  void shouldReturnSinglePendingAlertForMultipleAnalysis() {
    var result = recommendationDataAccess.selectPendingAlerts(2);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  @Sql
  void shouldInsertAlertRecommendation() {
    var result =
        recommendationDataAccess.insertAlertRecommendation(List.of(createInsertRequest()));
    assertThat(result.size()).isEqualTo(1);
    assertThat(result.get(0).getAlertId()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = { "JdbcRecommendationDataAccessIT.shouldInsertAlertRecommendation.sql" })
  void shouldNotInsertAlertRecommendation() {
    recommendationDataAccess.insertAlertRecommendation(List.of(createInsertRequest()));
    recommendationDataAccess.insertAlertRecommendation(List.of(createInsertRequest()));
    assertThat(jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM ae_recommendation",
        Integer.class)).isEqualTo(1);
  }

  private static void assertContextData(List<AlertRecommendation> alerts) {
    var alertContext = alerts
        .stream()
        .filter(a -> a
            .getAlertContext()
            .getAlertId()
            .equals("AVIR128SCR13925IN123TEST0003:IN:GR-ESAN:273067"))
        .findFirst()
        .get()
        .getAlertContext();

    assertThat(alertContext.getAlertId())
        .isEqualTo("AVIR128SCR13925IN123TEST0003:IN:GR-ESAN:273067");
    assertThat(alertContext.getMatches())
        .hasSize(2)
        .extracting(MatchContext::getMatchId)
        .containsExactly("GSN00067068", "DB00051992");
  }
}
