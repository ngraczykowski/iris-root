package com.silenteight.warehouse.management.group.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CountryGroupResource {

  public static final String ID_PARAM = "id";
  public static final String COUNTRY_GROUPS_URL = "/v1/countryGroups";
  public static final String COUNTRY_GROUP_URL = COUNTRY_GROUPS_URL + "/{" + ID_PARAM + "}";
}
