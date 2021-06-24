package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

  @Test
  @Sql
  void shouldSelectAllReadyRecommendations() {
    var result = recommendationDataAccess.selectPendingAlerts(1);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = { "JdbcRecommendationDataAccessIT.context.sql" })
  void shouldSelectAlertsContextFromAnalysis() {
    var alerts = recommendationDataAccess.selectAlertRecommendation(1);
    assertThat(alerts.size()).isEqualTo(2);
    var alertContext = alerts.get(0);
    assertContextData(alertContext.getAlertContext());
  }

  @Test
  @Sql(scripts = { "JdbcRecommendationDataAccessIT.context.sql" })
  void shouldSelectAlertsContextFromAnalysisAndDataset() {
    var alerts = recommendationDataAccess.selectAlertRecommendation(1, 1);
    assertThat(alerts.size()).isEqualTo(1);
    var alertContext = alerts.get(0);
    assertContextData(alertContext.getAlertContext());
  }

  private void assertContextData(AlertContext alertContext) {
    assertThat(alertContext.getAlertId())
        .isEqualTo("AVIR128SCR13925IN123TEST0003:IN:GR-ESAN:273067");
    assertThat(alertContext.getMatches().size()).isEqualTo(2);
    var match = alertContext.getMatches().get(1);
    assertThat(match.getMatchId()).isEqualTo("DB00051992");
    assertThat(match.getSolution()).isEqualTo("SOLUTION_NO_DECISION");
    assertThat(match.getFeatures().get("features/dob").getAgentName())
        .isEqualTo("agents/date");
  }
}
