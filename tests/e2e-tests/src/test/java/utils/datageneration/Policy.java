package utils.datageneration;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Policy {
  String uuid;
  String name;
  String state;
  List<PolicyStep> steps;
  String creationPayload;
}
