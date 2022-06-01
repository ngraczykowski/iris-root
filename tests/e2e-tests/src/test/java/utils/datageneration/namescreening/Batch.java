package utils.datageneration.namescreening;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class Batch {

  String id;
  String status;
  String payload;
  String generationStartTime;
  List<AlertDataSource> alertDataSources;
}
