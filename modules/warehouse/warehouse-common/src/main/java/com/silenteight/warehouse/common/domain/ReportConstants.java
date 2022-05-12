package com.silenteight.warehouse.common.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportConstants {

  public static final String PRODUCTION = "production";
  public static final String SIMULATION = "simulation";

  public static final Predicate<String> IS_PRODUCTION = PRODUCTION::equalsIgnoreCase;
}
