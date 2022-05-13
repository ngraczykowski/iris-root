package utils.datageneration;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Feature {
  String name;
  String condition;
  List<String> values;
}
