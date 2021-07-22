package com.silenteight.payments.bridge.dto.output;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ActionDetailsDto {
  @JsonProperty("DateTime")
  String dateTime;
  @JsonProperty("Comment")
  String comment;
  @JsonProperty("Operator")
  String operator;
  @JsonProperty("StatusName")
  String statusName;
  @JsonProperty("Attachment")
  AttachmentDto attachment;
}
