package com.silenteight.payments.bridge.common.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentsUtils {

  public static final String FEATURE_PREFIX = "features/";

  public static final String HISTORICAL_RISK_CUSTOMER_NAME_FEATURE = "historicalRiskCustomerName";
  // TODO: remove when decide that customer name will not be used
  public static final String HISTORICAL_RISK_CUSTOMER_NAME_LEARNING_DISC = "hist_customer_name";

  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_TP =
      "historicalRiskAccountNumberTP";
  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_FP =
      "historicalRiskAccountNumberFP";
  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP =
      "hist_account_number_fp";
  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP =
      "hist_account_number_tp";

  public static final String CONTEXTUAL_LEARNING_FEATURE_TP =
      "contextualLearningNameTP";
  public static final String CONTEXTUAL_LEARNING_FEATURE_FP =
      "contextualLearningNameFP";
  public static final String CONTEXTUAL_LEARNING_FEATURE_NAME_TP =
      FEATURE_PREFIX + CONTEXTUAL_LEARNING_FEATURE_TP;
  public static final String CONTEXTUAL_LEARNING_FEATURE_NAME_FP =
      FEATURE_PREFIX + CONTEXTUAL_LEARNING_FEATURE_FP;
  public static final String CONTEXTUAL_LEARNING_DISC_TP = "hist_contextual_learning_name_tp";
  public static final String CONTEXTUAL_LEARNING_DISC_FP = "hist_contextual_learning_name_fp";

  public static final String GEO_FEATURE = "geo";

  public static final String BANK_IDENTIFICATION_CODES_FEATURE = "bankIdentificationCodes";

  public static final String NAME_FEATURE = "name";

  public static final String NAME_TEXT_FEATURE = "nameMatchedText";

  public static final String ORGANIZATION_NAME_FEATURE = "organizationName";

}
