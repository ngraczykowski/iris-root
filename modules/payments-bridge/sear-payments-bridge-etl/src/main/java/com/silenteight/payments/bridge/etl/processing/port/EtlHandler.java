package com.silenteight.payments.bridge.etl.processing.port;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.comments.api.v2.CommentInput;

public interface EtlHandler {

  void registerAgentInput(AgentInput agentInput);

  void registerCategoryValue(CategoryValue categoryValue);

  void registerCommentInput(CommentInput commentInput);
}
