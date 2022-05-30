package com.silenteight.adjudication.engine.solving.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.io.Serializable;

@Getter
@Builder
@Value
public class CategoryValue implements Serializable {

  private static final long serialVersionUID = 554276097928977893L;
  String category;
  String value;
}
