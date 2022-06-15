package com.silenteight.adjudication.engine.solving.data.jdbc;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.solving.data.*;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

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

  @Bean
  MatchSolutionEntityExtractor matchSolutionEntityExtractor(
      ProtoMessageToObjectNodeConverter converter) {
    return new MatchSolutionEntityExtractor(
        converter, JsonConversionHelper.INSTANCE.objectMapper());
  }

  @Bean
  MatchSolutionStore matchSolutionStore(
      JdbcTemplate jdbcTemplate, MatchSolutionEntityExtractor matchSolutionEntityExtractor) {
    return new JdbcMatchSolutionStore(
        new MatchSolutionJdbcRepository(jdbcTemplate), matchSolutionEntityExtractor);
  }
}
