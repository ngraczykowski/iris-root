package utils.datageneration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class Feature {
  String name;
  String condition;
  String values;
}
