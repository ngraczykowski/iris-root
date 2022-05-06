package com.silenteight.connector.ftcc.common.dto.output;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class AttachmentDto {

  String name;

  String contents;

  String comments;
}
