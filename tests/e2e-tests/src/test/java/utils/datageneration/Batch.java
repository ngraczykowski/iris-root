package utils.datageneration;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Batch {
  String id;
  String status;
  String payload;
  String generationStartTime;
}
