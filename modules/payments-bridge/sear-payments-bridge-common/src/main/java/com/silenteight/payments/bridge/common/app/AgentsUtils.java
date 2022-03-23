package com.silenteight.payments.bridge.common.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentsUtils {

  public static final String FEATURE_PREFIX = "features/";

  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_TP =
      FEATURE_PREFIX + "historicalRiskAccountNumberTP";
  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_FP =
      FEATURE_PREFIX + "historicalRiskAccountNumberFP";
  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP =
      "hist_account_number_fp";
  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP =
      "hist_account_number_tp";

  public static final String CONTEXTUAL_LEARNING_FEATURE_NAME_TP =
      FEATURE_PREFIX + "contextualLearningNameTP";
  public static final String CONTEXTUAL_LEARNING_FEATURE_NAME_FP =
      FEATURE_PREFIX + "contextualLearningNameFP";
  public static final String CONTEXTUAL_LEARNING_DISC_TP = "hist_contextual_learning_name_tp";
  public static final String CONTEXTUAL_LEARNING_DISC_FP = "hist_contextual_learning_name_fp";

  public static final String GEO_FEATURE_NAME = FEATURE_PREFIX + "geo";

  public static final String BANK_IDENTIFICATION_CODES_FEATURE_NAME =
      FEATURE_PREFIX + "bankIdentificationCodes";

  public static final String NAME_FEATURE_NAME = FEATURE_PREFIX + "name";

  public static final String NAME_TEXT_FEATURE_NAME = FEATURE_PREFIX + "nameMatchedText";

  public static final String ORGANIZATION_NAME_FEATURE_NAME = FEATURE_PREFIX + "organizationName";

}
