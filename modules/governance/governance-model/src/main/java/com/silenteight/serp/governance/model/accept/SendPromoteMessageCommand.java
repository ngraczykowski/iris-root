package com.silenteight.serp.governance.model.accept;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.model.api.v1.ModelPromotedForProduction;

import java.util.UUID;

@Value(staticConstructor = "of")
public class SendPromoteMessageCommand {
  @NonNull
  UUID correlationId;
  @NonNull
  String modelName;

  ModelPromotedForProduction toMessage() {
    return ModelPromotedForProduction
        .newBuilder()
        .setName(modelName)
        .build();
  }
}
