package com.silenteight.payments.bridge.common.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.common.dto.common.StatusInfoDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class AlertDecisionMessageDto implements Serializable {

  private static final long serialVersionUID = -6851877425739319284L;

  private String unit;

  private String businessUnit;

  private String messageId;

  private String systemId;

  private StatusInfoDto status;

  private String comment;

  private String operator;

  @JsonInclude(Include.NON_NULL)
  private AttachmentDto attachment;

  private List<AlertDecisionActionDto> actions;
}
