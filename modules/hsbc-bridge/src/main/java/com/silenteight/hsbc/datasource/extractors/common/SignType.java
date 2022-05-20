package com.silenteight.hsbc.datasource.extractors.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SignType {
  SPACE(" "),
  SEMICOLON(";"),
  COMMA(","),
  PIPE("|");

  private final String sign;
}
