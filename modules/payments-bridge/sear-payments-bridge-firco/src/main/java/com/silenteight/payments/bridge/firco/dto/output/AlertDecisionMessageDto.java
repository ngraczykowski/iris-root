package com.silenteight.payments.bridge.firco.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.firco.dto.common.StatusInfoDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertDecisionMessageDto implements Serializable {

  private static final long serialVersionUID = -6851877425739319284L;

  @JsonProperty("Unit")
  private String unit;

  @JsonProperty("BusinessUnit")
  private String businessUnit;

  @JsonProperty("MessageID")
  private String messageId;

  @JsonProperty("SystemID")
  private String systemId;

  @JsonProperty("Status")
  private StatusInfoDto status;

  @JsonProperty("Comment")
  private String comment;

  @JsonProperty("Operator")
  private String operator;

  @JsonProperty("Attachment")
  @JsonInclude(Include.NON_NULL)
  private AttachmentDto attachment;

  @JsonProperty("Actions")
  private List<AlertDecisionActionDto> actions;
}
