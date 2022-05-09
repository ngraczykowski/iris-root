package utils.datageneration;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AlertDataSource {
  String alertId;
  String flagKey;
  String alertDate;
  String caseId;
  String currentState;
}
