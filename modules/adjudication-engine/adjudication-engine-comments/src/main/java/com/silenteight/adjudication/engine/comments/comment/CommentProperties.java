/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.comments.comment;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("ae.comments")
@Data
@Validated
class CommentProperties {

  private boolean shouldCacheTemplates = true;

  private String environment = "sierra";
}
