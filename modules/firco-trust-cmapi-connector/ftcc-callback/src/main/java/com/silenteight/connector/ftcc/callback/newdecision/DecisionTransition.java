package com.silenteight.connector.ftcc.callback.newdecision;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.opencsv.bean.CsvBindByName;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class DecisionTransition {

  @CsvBindByName(required = true, column = "SourceState")
  private String sourceState;
  @CsvBindByName(required = true, column = "Recommendation")
  private String recommendation;
  @CsvBindByName(required = true, column = "DestinationState")
  private String destinationState;
}
