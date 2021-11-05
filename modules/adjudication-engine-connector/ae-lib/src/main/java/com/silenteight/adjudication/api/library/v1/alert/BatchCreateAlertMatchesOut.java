package com.silenteight.adjudication.api.library.v1.alert;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class BatchCreateAlertMatchesOut {

  List<AlertMatchOut> alertMatches;
}
