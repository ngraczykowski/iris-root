package com.silenteight.serp.governance.solutiondiscrepancy;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("serp.governance.solution-discrepancy")
@Data
@Slf4j
public class SolutionDiscrepancyProperties {

  private boolean dryRunEnabled = true;

}
