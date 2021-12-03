package com.silenteight.payments.bridge.common.dto.output;

import lombok.Data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(UpperCamelCaseStrategy.class)
public class ActionDetailsDto {

  private String dateTime;

  private String comment;

  private String operator;

  private String statusName;

  private AttachmentDto attachment;
}
