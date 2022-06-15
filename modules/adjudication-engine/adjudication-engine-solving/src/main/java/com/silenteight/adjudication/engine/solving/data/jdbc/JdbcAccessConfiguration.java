package com.silenteight.adjudication.engine.solving.data.jdbc;

import com.silenteight.adjudication.engine.solving.data.CommentInputDataAccess;
import com.silenteight.adjudication.engine.solving.data.MatchCategoryDataAccess;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureStoreDataAccess;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class JdbcAccessConfiguration {

  @Bean
  MatchFeatureDataAccess matchFeatureDataAccess(
      SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery,
      SelectAnalysisMatchFeaturesQuery selectAnalysisMatchFeaturesQuery,
      SelectAnalysisMatchCategoriesQuery selectAnalysisMatchCategoriesQuery,
      SelectAnalysisAlertLabelsQuery selectAnalysisAlertLabelsQuery) {
    return new JdbcMatchFeaturesDataAccess(
        selectAnalysisFeaturesQuery,
        selectAnalysisMatchFeaturesQuery,
        selectAnalysisMatchCategoriesQuery,
        selectAnalysisAlertLabelsQuery);
  }

  @Bean
  SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery(NamedParameterJdbcTemplate jdbcTemplate) {
    return new SelectAnalysisFeaturesQuery(jdbcTemplate);
  }

  @Bean
  SelectAnalysisAlertLabelsQuery selectAnalysisAlertLabelsQuery(
      NamedParameterJdbcTemplate jdbcTemplate) {
    return new SelectAnalysisAlertLabelsQuery(jdbcTemplate);
  }

  @Bean
  SelectAnalysisMatchFeaturesQuery selectAnalysisMatchFeaturesQuery(
      NamedParameterJdbcTemplate jdbcTemplate) {
    return new SelectAnalysisMatchFeaturesQuery(jdbcTemplate);
  }

  @Bean
  SelectAnalysisMatchCategoriesQuery selectAnalysisMatchCategoriesQuery(
      NamedParameterJdbcTemplate jdbcTemplate) {
    return new SelectAnalysisMatchCategoriesQuery(jdbcTemplate);
  }

  @Bean
  CommentInputDataAccess commentInputStoreService(JdbcTemplate jdbcTemplate) {
    return new JdbcCommentInputStoreDataAccess(new CommentInputJdbcRepository(jdbcTemplate));
  }

  @Bean
  MatchCategoryDataAccess matchCategoryDataAccess(JdbcTemplate jdbcTemplate) {
    return new JdbcMatchCategoryDataAccess(new MatchCategoryJdbcRepository(jdbcTemplate));
  }

  @Bean
  MatchFeatureStoreDataAccess matchFeatureStoreDataAccess(JdbcTemplate jdbcTemplate) {
    return new JdbcMatchFeatureStoreDataAccess(new MatchFeatureJdbcRepository(jdbcTemplate));
  }
}
