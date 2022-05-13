package utils.datageneration;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class PolicyStep {
  String id;
  String name;
  String solution;
  List<Feature> featureList;
  String templatedFeatureList;
}
