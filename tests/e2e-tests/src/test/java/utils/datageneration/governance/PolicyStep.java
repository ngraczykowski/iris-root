package utils.datageneration.governance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class PolicyStep {

  String uuid;
  String name;
  String solution;
  List<Feature> featureList;
  String templatedFeatureList;
}
