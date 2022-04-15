package com.silenteight.serp.governance.policy.transform.rbs;

import com.silenteight.serp.governance.policy.domain.dto.Solution;
import com.silenteight.serp.governance.policy.transform.rbs.StepsData.Feature;
import com.silenteight.serp.governance.policy.transform.rbs.StepsData.Step;

import java.net.URL;
import java.util.UUID;

import static com.google.common.io.Resources.getResource;
import static java.util.List.of;
import static java.util.UUID.randomUUID;

class RbsImportFixture {

  static final UUID POLICY_UUID = randomUUID();
  static final String IMPORT_FILE_NAME = "rbs.csv";
  static final String INVALID_IMPORT_FILE_NAME = "rbs_invalid.csv";

  static final URL IMPORT_FILE_URL = getResource("rbs/" + IMPORT_FILE_NAME);
  static final URL INVALID_IMPORT_FILE_URL = getResource("rbs/" + INVALID_IMPORT_FILE_NAME);

  static final String FEATURE_AP_TYPE = "categories/apType";
  static final String FEATURE_NAME_AGENT = "features/nameAgent";
  static final StepsData STEPS_DATA = StepsData.builder()
      .name(IMPORT_FILE_NAME)
      .steps(of(
          Step.builder()
              .solution(Solution.POTENTIAL_TRUE_POSITIVE)
              .reasoningBranchId("1")
              .features(of(
                  new Feature(FEATURE_AP_TYPE, "C"),
                  new Feature(FEATURE_NAME_AGENT, "MATCH")))
              .build(),
          Step.builder()
              .solution(Solution.FALSE_POSITIVE)
              .reasoningBranchId("2")
              .features(of(
                  new Feature(FEATURE_AP_TYPE, "I"),
                  new Feature(FEATURE_NAME_AGENT, "HQ_NO_MATCH")))
              .build()))
      .build();
}
