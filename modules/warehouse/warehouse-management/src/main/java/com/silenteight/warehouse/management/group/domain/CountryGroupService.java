package com.silenteight.warehouse.management.group.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.domain.group.CountryGroupEntity;
import com.silenteight.warehouse.common.domain.group.CountryGroupRepository;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupAlreadyExistsException;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupDoesNotExistException;
import com.silenteight.warehouse.management.group.update.dto.UpdateCountryGroupRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
public class CountryGroupService {

  @NonNull
  private final CountryGroupRepository repository;

  @Transactional
  public void create(CountryGroupDto dto) {
    try {
      doCreate(dto);
    } catch (DataIntegrityViolationException e) {
      throw new CountryGroupAlreadyExistsException(dto.getId(), e);
    }
  }

  @Transactional
  public void update(@NonNull UUID id, @NonNull UpdateCountryGroupRequest request) {
    CountryGroupEntity countryGroup = repository
        .findByCountryGroupId(id)
        .orElseThrow(() -> new CountryGroupDoesNotExistException(id));

    countryGroup.updateName(request.getName());
    repository.save(countryGroup);
  }

  private void doCreate(CountryGroupDto dto) {
    CountryGroupEntity entity = CountryGroupEntity.builder()
        .countryGroupId(dto.getId())
        .name(dto.getName())
        .migrated(true)
        .build();

    repository.save(entity);
  }

  @Transactional
  public void delete(UUID id) {
    if (!repository.existsByCountryGroupId(id))
      throw new CountryGroupDoesNotExistException(id);

    doDelete(id);
  }

  private void doDelete(UUID id) {
    repository.deleteByCountryGroupId(id);
  }
}
