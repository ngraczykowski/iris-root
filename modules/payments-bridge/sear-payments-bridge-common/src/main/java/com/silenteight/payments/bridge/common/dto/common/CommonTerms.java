package com.silenteight.payments.bridge.common.dto.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonTerms {

  public static final String TAG_ORIGINATOR = "ORIGINATOR";
  public static final String TAG_BENE = "BENE";
  public static final String TAG_ORGBANK = "ORGBANK";
  public static final String TAG_INSBANK = "INSBANK";
  public static final String TAG_50F = "50F";
  public static final String TAG_RECEIVBANK = "RECEIVBANK";
  public static final String TAG_50K = "50K";
  public static final String TAG_59 = "59";
  public static final String TAG_50 = "50";
  public static final String TAG_70 = "70";
  public static final String TAG_INSMN = "INSMN";

  public static final String APPLICATION_CODE_GTEX = "GTEX";
  public static final String APPLICATION_CODE_GFX = "GFX";

  public static final String FIRCO_FORMAT_SWF = "SWF";
  public static final String FIRCO_FORMAT_IAT_O = "IAT-O";
  public static final String FIRCO_FORMAT_FED = "FED";
  public static final String FIRCO_FORMAT_INT = "INT";
  public static final String FIRCO_FORMAT_IAT_I = "IAT-I";
  public static final String FIRCO_FORMAT_O_F = "O-F";

  public static final int LINE_1 = 0;
  public static final int LINE_2 = 1;
  public static final int LINE_3 = 2;
  public static final int LINE_4 = 3;
  public static final int LINE_5 = 4;

  public static final String NAME_ROW_PREFIX = "1/";
  public static final String ADDRESS_ROW_PREFIX = "2/";
  public static final String COUNTRY_ROW_PREFIX = "3/";

  public static final String COUNTRY_TOWN = "countryTown";
  public static final String ADDRESS = "address";
  public static final String NAME = "name";
}
