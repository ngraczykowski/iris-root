package com.silenteight.payments.bridge.common.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentsUtils {

  public static final String FEATURE_PREFIX = "features/";

  public static final String HISTORICAL_RISK_CUSTOMER_NAME_FEATURE = "historicalRiskCustomerName";
  public static final String HISTORICAL_RISK_CUSTOMER_NAME_LEARNING_DISC = "hist_customer_name";
  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE =
      "historicalRiskAccountNumber";
  public static final String HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC = "hist_account_number";

  public static final String GEO_FEATURE = "geo";

  public static final String BANK_IDENTIFICATION_CODES_FEATURE = "bankIdentificationCodes";

  public static final String NAME_FEATURE = "name";

  public static final String NAME_TEXT_FEATURE = "nameMatchedText";

  public static final String ORGANIZATION_NAME_FEATURE = "organizationName";

}
