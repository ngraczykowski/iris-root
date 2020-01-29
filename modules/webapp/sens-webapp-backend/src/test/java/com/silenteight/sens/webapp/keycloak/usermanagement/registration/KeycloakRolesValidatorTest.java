package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptySet;
import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakRolesValidatorTest {

  @Mock
  private RolesResource rolesResource;

  @InjectMocks
  private KeycloakRolesValidator underTest;

  @Test
  void noRolesInKeycloak_returnsFalse() {
    boolean actual = underTest.rolesExist(of(Fixtures.ANALYST));

    assertFalse(actual);
  }

  @Test
  void emptySetPassed_throwsException() {
    Executable when = () -> underTest.rolesExist(emptySet());

    assertThrows(IllegalArgumentException.class, when);
  }

  @Nested
  class GivenAnalystAndMakerRoleInKeycloak {

    @BeforeEach
    void setUp() {
      given(rolesResource.list()).willReturn(List.of(Fixtures.ANALYST_ROLE, Fixtures.MAKER_ROLE));
    }

    @Test
    void approverAndMakerRolesPassed_returnsFalse() {
      boolean actual = underTest.rolesExist(of(Fixtures.APPROVER, Fixtures.MAKER));

      assertFalse(actual);
    }

    @Test
    void analystRolePassed_returnsTrue() {
      boolean actual = underTest.rolesExist(of(Fixtures.ANALYST));

      assertTrue(actual);
    }

    @Test
    void approverRolePassed_returnsFalse() {
      boolean actual = underTest.rolesExist(of(Fixtures.APPROVER));

      assertFalse(actual);
    }

    @Test
    void analystAndMakerRolesPassed_returnsTrue() {
      boolean actual = underTest.rolesExist(of(Fixtures.ANALYST, Fixtures.MAKER));

      assertTrue(actual);
    }
  }

  @NoArgsConstructor(access = AccessLevel.NONE)
  private static final class Fixtures {

    static final String ANALYST = "Analyst";
    static final String MAKER = "Maker";
    static final String APPROVER = "Approver";

    static final RoleRepresentation ANALYST_ROLE = new RoleRepresentation(ANALYST, null, false);
    static final RoleRepresentation MAKER_ROLE = new RoleRepresentation(MAKER, null, false);
  }
}
