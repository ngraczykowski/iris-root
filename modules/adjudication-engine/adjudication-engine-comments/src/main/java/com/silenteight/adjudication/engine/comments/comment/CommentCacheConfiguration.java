/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(CommentProperties.class)
@Configuration("commentCacheConfiguration")
@RequiredArgsConstructor
class CommentCacheConfiguration {

  private final CommentProperties commentProperties;

  public boolean shouldCacheCommentTemplates() {
    return commentProperties.isShouldCacheTemplates();
  }
}
