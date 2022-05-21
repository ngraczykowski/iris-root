package com.silenteight.hsbc.bridge.alert.dto;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.alert.AlertStatus;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Builder
@Value
public class AlertEntityDto {

  String externalId;
  String name;
  String discriminator;
  OffsetDateTime alertTime;
  String errorMessage;
  String bulkId;
  AlertStatus status;
  Collection<AlertMatchEntityDto> matches;
  byte[] payload;
  List<AlertMetadataDto> metadata;
}
