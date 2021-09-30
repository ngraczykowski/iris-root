package com.silenteight.payments.bridge.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.common.dto.common.StatusInfoDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
class ActionDto implements Serializable {

  private static final long serialVersionUID = -1149520199768792368L;

  private String dateTime; // "20200909074059"

  private String operator; // "Filter Engine"

  private String comment; // "Stop: 2, Nonblocking: 0"

  private StatusInfoDto status;
}
