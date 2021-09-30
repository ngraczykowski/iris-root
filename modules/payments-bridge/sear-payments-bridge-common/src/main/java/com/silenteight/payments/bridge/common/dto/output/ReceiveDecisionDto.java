package com.silenteight.payments.bridge.common.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class ReceiveDecisionDto implements Serializable {

  private static final long serialVersionUID = -4284460925751955090L;

  private String versionTag = "1";

  private FircoAuthenticationDto authentication;

  private List<ReceiveDecisionMessageDto> messages;
}
