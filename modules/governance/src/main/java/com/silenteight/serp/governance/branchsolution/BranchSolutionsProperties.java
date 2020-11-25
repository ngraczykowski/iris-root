package com.silenteight.serp.governance.branchsolution;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "serp.governance.branch-solution")
class BranchSolutionsProperties {

  private boolean hintedEnabled = false;
}
