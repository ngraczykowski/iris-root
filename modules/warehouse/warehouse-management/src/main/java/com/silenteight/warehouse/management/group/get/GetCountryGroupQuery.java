package com.silenteight.warehouse.management.group.get;

import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

import java.util.UUID;

public interface GetCountryGroupQuery {

  CountryGroupDto get(UUID id);
}
