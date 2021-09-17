package com.silenteight.universaldatasource.app.commentinput.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.protocol.MessageRegistry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import protobuf.ProtoMessageToObjectNodeConverter;

@RequiredArgsConstructor
@Configuration
class JdbcCommentInputConfiguration {

  private final MessageRegistry messageRegistry;

  private final JdbcTemplate jdbcTemplate;

  @Bean
  ProtoMessageToObjectNodeConverter protoMessageToObjectNodeConverter() {
    return new ProtoMessageToObjectNodeConverter(messageRegistry);
  }

  @Bean
  InsertCommentInputsQuery insertCommentInputsQuery() {
    return new InsertCommentInputsQuery(jdbcTemplate);
  }

}
