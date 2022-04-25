package com.silenteight.fab.dataprep.domain.model;

import lombok.Value;

@Value
public class AlertErrorDescription {

  public static final AlertErrorDescription EXTRACTION =
      new AlertErrorDescription("Failed to extract alerts and matches.");

  public static final AlertErrorDescription CREATE_FEATURE_INPUT =
      new AlertErrorDescription("Failed to create feature inputs.");

  public static final AlertErrorDescription GETTING_FROM_DB =
      new AlertErrorDescription("Failed to get alert from the internal db.");

  public static final AlertErrorDescription NONE =
      new AlertErrorDescription("");

  String description;
}
