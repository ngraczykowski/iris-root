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
public class CodeDto implements Serializable {

  private static final long serialVersionUID = -5273726119096949439L;

  private String name; // "KDBKKPPY"

  private String type; // "Bic"
}
