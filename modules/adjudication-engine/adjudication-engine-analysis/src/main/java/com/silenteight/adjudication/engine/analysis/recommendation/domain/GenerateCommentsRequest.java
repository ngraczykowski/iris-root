package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Value;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;

@Value
public class GenerateCommentsRequest {

  AlertContext alertContext;
}
