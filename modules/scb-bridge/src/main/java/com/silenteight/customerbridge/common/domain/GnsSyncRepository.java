package com.silenteight.customerbridge.common.domain;

import org.springframework.data.repository.Repository;

import java.util.List;

interface GnsSyncRepository extends Repository<GnsSync, Long> {

  List<GnsSync> findAllBySyncModeAndFinishedAtIsNull(String syncMode);

  GnsSync getById(long id);

  void save(GnsSync entity);
}
