package com.silenteight.universaldatasource.app.commentinput.adapter.incoming.v1;

import lombok.RequiredArgsConstructor;

import com.silenteight.universaldatasource.app.commentinput.port.incoming.StreamCommentInputsUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class CommentInputAdapterConfiguration {

  private final StreamCommentInputsUseCase streamCommentInputsUseCase;

  private final CommentInputVersionMapper versionMapper;

  @Bean(name = "commentInputAdapterV1")
  CommentInputAdapter commentInputAdapter() {
    return new CommentInputAdapter(streamCommentInputsUseCase, versionMapper);
  }
}
