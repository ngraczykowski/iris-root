package com.silenteight.payments.bridge.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto implements Serializable {

  private static final long serialVersionUID = -6851877425739319284L;
  @JsonProperty("Unit")
  String unit;

  @JsonProperty("BusinessUnit")
  String businessUnit;

  @JsonProperty("MessageID")
  String messageId;

  @JsonProperty("SystemID")
  String systemId;

  @JsonProperty("Status")
  StatusToSendDto statusDto;

  @JsonProperty("Comment")
  String comment;

  @JsonProperty("Operator")
  String operator;

  @JsonProperty("Attachment")
  @JsonInclude(Include.NON_NULL)
  AttachmentDto attachment;

  @JsonProperty("Actions")
  List<ActionToSendDto> actions;
}
