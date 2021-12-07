package com.silenteight.registration.api.library.v1.messages;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MessageBatchCompleted {

  String batchId;
  List<String> alertIds;
}
