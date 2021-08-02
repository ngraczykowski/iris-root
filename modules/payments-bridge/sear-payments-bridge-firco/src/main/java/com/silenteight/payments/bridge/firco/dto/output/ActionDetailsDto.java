package com.silenteight.payments.bridge.firco.dto.output;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ActionDetailsDto {

  @JsonProperty("DateTime")
  private String dateTime;

  @JsonProperty("Comment")
  private String comment;

  @JsonProperty("Operator")
  private String operator;

  @JsonProperty("StatusName")
  private String statusName;

  @JsonProperty("Attachment")
  private AttachmentDto attachment;
}
