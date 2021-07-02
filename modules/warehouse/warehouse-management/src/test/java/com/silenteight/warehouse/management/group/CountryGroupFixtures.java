package com.silenteight.warehouse.management.group;

import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;
import com.silenteight.warehouse.management.group.update.dto.UpdateCountryGroupRequest;

import static com.silenteight.warehouse.common.opendistro.roles.RolesFixtures.COUNTRY_GROUP_ID;

public final class CountryGroupFixtures {

  public static final String COUNTRY_GROUP_URL = "/v1/countryGroups";
  public static final String NAME = "oceania";
  public static final String UPDATED_NAME = "europe";

  public static final CountryGroupDto COUNTRY_GROUP_DTO =
      CountryGroupDto.builder()
          .id(COUNTRY_GROUP_ID)
          .name(NAME)
          .build();

  public static final UpdateCountryGroupRequest UPDATE_COUNTRY_GROUP_REQUEST =
      new UpdateCountryGroupRequest(UPDATED_NAME);
}
