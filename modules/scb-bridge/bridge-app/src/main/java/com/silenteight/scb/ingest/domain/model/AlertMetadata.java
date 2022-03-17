package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AlertMetadata {

  String currentVersionId;

  @Builder.Default
  List<String> stopDescriptorNames = List.of();

  String datasetId;
  String datasetName;
  String uniqueCustId;
  String masterId;
  String busDate;
}
