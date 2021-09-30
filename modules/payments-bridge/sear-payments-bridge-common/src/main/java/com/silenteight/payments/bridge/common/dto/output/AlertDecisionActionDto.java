package com.silenteight.payments.bridge.common.dto.output;

import lombok.Data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@Data
@JsonSerialize(using = AlertDecisionActionDtoSerializer.class)
public class AlertDecisionActionDto implements Serializable {

  private static final long serialVersionUID = -4215114603801984462L;
}
