package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.user.sync.analyst.dto.ExternalAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.dto.InternalAnalyst;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SyncAnalystService {

  private final AnalystQuery analystQuery;
  private final ExternalAnalystRepository externalAnalystRepository;
  private final AnalystSynchronizer analystSynchronizer = new AnalystSynchronizer();

  public void synchronize() {
    List<InternalAnalyst> internalAnalysts = analystQuery.list();
    List<ExternalAnalyst> externalAnalysts = externalAnalystRepository.list();
    SynchronizedAnalysts result =
        analystSynchronizer.synchronize(internalAnalysts, externalAnalysts);

    // WA-247(mmastylo) implement, when Keycloak classes will be available
  }
}
