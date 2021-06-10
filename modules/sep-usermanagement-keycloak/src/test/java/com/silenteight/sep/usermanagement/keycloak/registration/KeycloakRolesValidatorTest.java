package com.silenteight.sep.usermanagement.keycloak.registration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sep.usermanagement.api.RolesValidator.RolesDontExistError;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptySet;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakRolesValidatorTest {

  @InjectMocks
  private KeycloakRolesValidator underTest;

  @Mock
  private ClientQuery clientQuery;
  @Mock
  private ClientsResource clientsResource;
  @Mock
  private ClientResource clientResource;
  @Mock
  private RoleMappingResource roleMappingResource;
  @Mock
  private RoleScopeResource roleScopeResource;

  @Test
  void noRolesInKeycloak_returnsError() {
    given(clientQuery.getByClientId(Fixtures.SCOPE)).willReturn(clientRepresentation());
    given(clientsResource.get(Fixtures.SCOPE)).willReturn(clientResource);
    given(clientResource.getScopeMappings()).willReturn(roleMappingResource);
    given(roleMappingResource.clientLevel(Fixtures.CLIENT_ID)).willReturn(roleScopeResource);

    Optional<RolesDontExistError> error = underTest.validate(Fixtures.SCOPE, of(Fixtures.ANALYST));

    assertThat(error).isNotEmpty();
  }

  @Test
  void emptySetPassed_throwsException() {
    Executable when = () -> underTest.validate(Fixtures.SCOPE, emptySet());

    assertThrows(IllegalArgumentException.class, when);
  }

  @NoArgsConstructor(access = AccessLevel.NONE)
  private static final class Fixtures {

    static final String CLIENT_ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
    static final String SCOPE = "frontend";
    static final String ANALYST = "Analyst";
    static final String MAKER = "Maker";
    static final String APPROVER = "Approver";

    static final RoleRepresentation ANALYST_ROLE = new RoleRepresentation(ANALYST, null, false);
    static final RoleRepresentation MAKER_ROLE = new RoleRepresentation(MAKER, null, false);
  }

  private static ClientRepresentation clientRepresentation() {
    ClientRepresentation client = new ClientRepresentation();
    client.setId(Fixtures.CLIENT_ID);
    client.setClientId(Fixtures.SCOPE);
    return client;
  }

  @Nested
  class GivenAnalystAndMakerRoleInKeycloak {

    @BeforeEach
    void setUp() {
      given(clientQuery.getByClientId(Fixtures.SCOPE)).willReturn(clientRepresentation());
      given(roleMappingResource.clientLevel(Fixtures.CLIENT_ID)).willReturn(roleScopeResource);
      given(roleScopeResource.listAll()).willReturn(
          List.of(Fixtures.ANALYST_ROLE, Fixtures.MAKER_ROLE));
    }

    @Test
    void approverAndMakerRolesPassed_returnsError() {
      given(clientsResource.get(Fixtures.SCOPE)).willReturn(clientResource);
      given(clientResource.getScopeMappings()).willReturn(roleMappingResource);
      Optional<RolesDontExistError> error =
          underTest.validate(Fixtures.SCOPE, of(Fixtures.APPROVER, Fixtures.MAKER));

      assertThat(error).isNotEmpty();
    }

    @Test
    void analystRolePassed_returnsNoError() {
      given(clientsResource.get(Fixtures.SCOPE)).willReturn(clientResource);
      given(clientResource.getScopeMappings()).willReturn(roleMappingResource);
      Optional<RolesDontExistError> error = underTest.validate(
          Fixtures.SCOPE, of(Fixtures.ANALYST));

      assertThat(error).isEmpty();
    }

    @Test
    void approverRolePassed_returnsError() {
      given(clientsResource.get(Fixtures.SCOPE)).willReturn(clientResource);
      given(clientResource.getScopeMappings()).willReturn(roleMappingResource);
      Optional<RolesDontExistError> error =
          underTest.validate(Fixtures.SCOPE, of(Fixtures.APPROVER));

      assertThat(error).isNotEmpty();
    }

    @Test
    void analystAndMakerRolesPassed_returnsNoError() {
      given(clientsResource.get(Fixtures.SCOPE)).willReturn(clientResource);
      given(clientResource.getScopeMappings()).willReturn(roleMappingResource);
      Optional<RolesDontExistError> error =
          underTest.validate(Fixtures.SCOPE, of(Fixtures.ANALYST, Fixtures.MAKER));

      assertThat(error).isEmpty();
    }
  }
}
