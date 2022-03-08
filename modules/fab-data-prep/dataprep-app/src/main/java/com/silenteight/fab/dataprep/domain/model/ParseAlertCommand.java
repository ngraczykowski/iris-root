package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParseAlertCommand {

  String payload;

}
