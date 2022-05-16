package utils.datageneration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class PolicyStep {
  String id;
  String name;
  String solution;
  List<Feature> featureList;
  String templatedFeatureList;
}
