package com.silenteight.fab.dataprep.domain.category;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BuildCategoryCommand {

  String matchName;
  String systemId;
}
