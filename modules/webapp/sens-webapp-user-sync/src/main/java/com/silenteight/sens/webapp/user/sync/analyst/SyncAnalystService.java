package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.user.sync.analyst.dto.Analyst;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class SyncAnalystService {

  private final UserListQuery userListQuery;
  private final ExternalAnalystRepository externalAnalystRepository;
  private final AnalystSynchronizer analystSynchronizer = new AnalystSynchronizer();

  public void synchronize() {
    Collection<UserDto> users = userListQuery.list();
    Collection<Analyst> analysts = externalAnalystRepository.list();
    SynchronizedAnalysts result = analystSynchronizer.synchronize(users, analysts);

    // WA-247(mmastylo) implement, when Keycloak classes will be available
  }
}
