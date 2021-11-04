package com.silenteight.adjudication.engine.comments.commentinput.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
class JdbcAlertCommentInputConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  InsertCommentInputQuery insertCommentInputQuery() {
    return new InsertCommentInputQuery(jdbcTemplate);
  }
}
