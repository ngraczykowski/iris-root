package utils.datageneration.simulations;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class AlertGenerationDate {
  String from;
  String to;
}
