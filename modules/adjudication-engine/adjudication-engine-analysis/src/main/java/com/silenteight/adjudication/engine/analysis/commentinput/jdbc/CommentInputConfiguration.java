package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CommentInputJdbcProperties.class)
public class CommentInputConfiguration {

  @Valid
  private final CommentInputJdbcProperties properties;

  private final JdbcTemplate jdbcTemplate;

  @Bean
  SelectMissingAlertCommentInputQuery selectMissingAlertCommentInputQuery() {
    return new SelectMissingAlertCommentInputQuery(
        jdbcTemplate, properties.getMissingBatchSizeSelect());
  }
}
