package com.silenteight.simulator.dataset.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
class DatasetLabelProperties {

  @NotBlank
  private String name;
  @NotNull
  private List<String> values;
}
