package com.silenteight.serp.governance.migrate;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

class DecisionTreeModelParser {

  static DecisionTreeMigrationRoot parseModel(String json) {
    try {
      return JsonConversionHelper
          .INSTANCE
          .objectMapper()
          .readValue(json, DecisionTreeMigrationRoot.class);
    } catch (Exception e) {
      throw new MigrationException(e);
    }
  }

  private DecisionTreeModelParser() {
  }

}
