package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import one.util.streamex.EntryStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.USERS;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RolesCacheUpdaterTest {

  @InjectMocks
  private RolesCacheUpdater underTest;

  @Mock
  private RolesFetcher rolesFetcher;

  @Mock
  private CachedRolesProvider cachedRolesProvider;

  @Test
  void updatesCorrectly() {
    given(rolesFetcher.fetch()).willReturn(USERS);

    underTest.update();

    EntryStream
        .of(USERS)
        .forKeyValue((userId, roles) -> then(cachedRolesProvider).should().update(userId, roles));
  }
}
