package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CommentInputJdbcProperties.class)
class CommentInputConfiguration {

  @Valid
  private final CommentInputJdbcProperties properties;

  private final JdbcTemplate jdbcTemplate;

  @Bean
  SelectMissingAlertCommentInputQuery selectMissingAlertCommentInputQuery() {
    return new SelectMissingAlertCommentInputQuery(
        jdbcTemplate, properties.getMissingBatchSizeSelect());
  }

  @Bean
  SelectCommentInputByAlertIdQuery selectCommentInputByAlertIdQuery(ObjectMapper objectMapper) {
    return new SelectCommentInputByAlertIdQuery(jdbcTemplate, objectMapper);
  }
}
