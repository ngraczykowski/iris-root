package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class KibanaIndexPatternAttributes {

  @Nullable
  String title;
  @Nullable
  String timeFieldName;
  @Nullable
  String fields;
}
