package com.silenteight.payments.bridge.firco.dto.output;

import lombok.Data;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
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
