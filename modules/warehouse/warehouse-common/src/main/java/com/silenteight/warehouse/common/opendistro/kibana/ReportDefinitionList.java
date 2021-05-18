package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ReportDefinitionList {

  @Nullable
  private List<ReportDefinition> data;
}
