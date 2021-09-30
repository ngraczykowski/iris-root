package com.silenteight.payments.bridge.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class RulesContextDto implements Serializable {

  private static final long serialVersionUID = -8919235980086784741L;

  private String type; // "1"

  private String priority; // "0"

  private String confidentiality; // "0"

  private String info; // "none"
}
