package com.silenteight.universaldatasource.app.commentinput.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Configuration
class JdbcCommentInputConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  InsertCommentInputsQuery insertCommentInputsQuery() {
    return new InsertCommentInputsQuery(jdbcTemplate);
  }

}
