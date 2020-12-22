package com.silenteight.serp.governance.migrate.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.google.protobuf.ByteString;

import java.util.Optional;

@Value
@Builder
public class ExportMatchGroupsRequest {

  @NonNull
  ByteString featuresSignature;

  String decisionTreeName;

  public Optional<String> getDecisionTreeName() {
    return Optional.ofNullable(decisionTreeName);
  }
}
