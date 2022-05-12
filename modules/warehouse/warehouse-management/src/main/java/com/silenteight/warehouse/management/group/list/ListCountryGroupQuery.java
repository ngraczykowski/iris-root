package com.silenteight.warehouse.management.group.list;

import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

import java.util.Collection;

public interface ListCountryGroupQuery {

  Collection<CountryGroupDto> listAll();
}
