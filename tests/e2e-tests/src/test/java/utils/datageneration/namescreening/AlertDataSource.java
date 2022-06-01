package utils.datageneration.namescreening;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AlertDataSource {

  String alertId;
  String flagKey;
  String alertDate;
  String caseId;
  String currentState;
}
