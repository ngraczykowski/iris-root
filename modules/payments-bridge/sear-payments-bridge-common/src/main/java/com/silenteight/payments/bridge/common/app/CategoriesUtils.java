package com.silenteight.payments.bridge.common.app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriesUtils {

  public static final String CATEGORY_PREFIX = "categories/";

  public static final String CATEGORY_CROSSMATCH = "crossmatch";
  public static final String CATEGORY_NAME_CROSSMATCH = CATEGORY_PREFIX + CATEGORY_CROSSMATCH;

  public static final String CATEGORY_SPECIFIC_TERMS = "specificTerms";
  public static final String CATEGORY_NAME_SPECIFIC_TERMS =
      CATEGORY_PREFIX + CATEGORY_SPECIFIC_TERMS;

  public static final String CATEGORY_SPECIFIC_TERMS_2 = "specificTerms2";
  public static final String CATEGORY_NAME_SPECIFIC_TERMS_2 =
      CATEGORY_PREFIX + CATEGORY_SPECIFIC_TERMS_2;

  public static final String CATEGORY_HISTORICAL_RISK_ASSESSMENT = "historicalRiskAssessment";
  public static final String CATEGORY_NAME_HISTORICAL_RISK_ASSESSMENT =
      CATEGORY_PREFIX + CATEGORY_HISTORICAL_RISK_ASSESSMENT;

  public static final String CATEGORY_WATCHLIST_TYPE = "watchlistType";
  public static final String CATEGORY_NAME_WATCHLIST_TYPE =
      CATEGORY_PREFIX + CATEGORY_WATCHLIST_TYPE;

  public static final String CATEGORY_MATCH_TYPE = "matchType";
  public static final String CATEGORY_NAME_MATCH_TYPE = CATEGORY_PREFIX + CATEGORY_MATCH_TYPE;

  public static final String CATEGORY_COMPANY_NAME_SURROUNDING = "companyNameSurrounding";
  public static final String CATEGORY_NAME_COMPANY_NAME_SURROUNDING =
      CATEGORY_PREFIX + CATEGORY_COMPANY_NAME_SURROUNDING;

  //TODO(jgajewski): remove CATEGORY_MESSAGE_STRUCTURE
  // and replace by CATEGORY_NAME_MESSAGE_STRUCTURE
  public static final String CATEGORY_MESSAGE_STRUCTURE = "messageStructure";
  public static final String CATEGORY_NAME_MESSAGE_STRUCTURE = CATEGORY_PREFIX + "messageStructure";

}
