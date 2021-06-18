package com.silenteight.warehouse.management.group;

import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;
import com.silenteight.warehouse.management.group.update.dto.UpdateCountryGroupRequest;

import java.util.UUID;

import static java.util.UUID.fromString;

public final class CountryGroupFixtures {

  public static final String COUNTRY_GROUP_URL = "/v1/countryGroups";
  public static final UUID UUID = fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
  public static final String NAME = "oceania";
  public static final String UPDATED_NAME = "europe";

  public static final CountryGroupDto COUNTRY_GROUP_DTO =
      CountryGroupDto.builder()
          .id(UUID)
          .name(NAME)
          .build();

  public static final UpdateCountryGroupRequest UPDATE_COUNTRY_GROUP_REQUEST =
      new UpdateCountryGroupRequest(UPDATED_NAME);
}
