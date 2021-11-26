package com.silenteight.adjudication.engine.analysis.pii.jdbc;

import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcPiiDataAccess.class,
    RemoveMatchFeatureValueReasonQuery.class,
    RemoveAlertCommentInputValueQuery.class,
    RemoveRecommendationMatchContextQuery.class
})
class JdbcPiiDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcPiiDataAccess dataAccess;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    dataAccess.removePiiData(List.of(1L));
  }

  @Test
  void shouldRemoveFeatureValueReason() {
    assertThat(jdbcTemplate.queryForObject("SELECT reason FROM ae_match_feature_value",
        String.class)).isEqualTo("{}");
  }

  @Test
  void shouldRemoveAlertCommentInputValue() {
    assertThat(jdbcTemplate.queryForObject("SELECT value FROM ae_alert_comment_input",
        String.class)).isEqualTo("{}");
  }

  @Test
  void shouldRemoveRecommendationMatchContext() {
    assertThat(jdbcTemplate.queryForObject("SELECT match_contexts FROM ae_recommendation",
        String.class)).isEqualTo("[]");
  }
}
