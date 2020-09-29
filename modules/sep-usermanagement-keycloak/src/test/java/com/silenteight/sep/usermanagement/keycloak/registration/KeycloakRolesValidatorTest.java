package com.silenteight.sep.usermanagement.keycloak.registration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sep.usermanagement.api.RolesValidator.RolesDontExistError;

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
import java.util.Optional;

import static java.util.Collections.emptySet;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakRolesValidatorTest {

  @Mock
  private RolesResource rolesResource;

  @InjectMocks
  private KeycloakRolesValidator underTest;

  @Test
  void noRolesInKeycloak_returnsError() {
    Optional<RolesDontExistError> error = underTest.validate(of(Fixtures.ANALYST));

    assertThat(error).isNotEmpty();
  }

  @Test
  void emptySetPassed_throwsException() {
    Executable when = () -> underTest.validate(emptySet());

    assertThrows(IllegalArgumentException.class, when);
  }

  @NoArgsConstructor(access = AccessLevel.NONE)
  private static final class Fixtures {

    static final String ANALYST = "Analyst";
    static final String MAKER = "Maker";
    static final String APPROVER = "Approver";

    static final RoleRepresentation ANALYST_ROLE = new RoleRepresentation(ANALYST, null, false);
    static final RoleRepresentation MAKER_ROLE = new RoleRepresentation(MAKER, null, false);
  }

  @Nested
  class GivenAnalystAndMakerRoleInKeycloak {

    @BeforeEach
    void setUp() {
      given(rolesResource.list()).willReturn(List.of(Fixtures.ANALYST_ROLE, Fixtures.MAKER_ROLE));
    }

    @Test
    void approverAndMakerRolesPassed_returnsError() {
      Optional<RolesDontExistError> error =
          underTest.validate(of(Fixtures.APPROVER, Fixtures.MAKER));

      assertThat(error).isNotEmpty();
    }

    @Test
    void analystRolePassed_returnsNoError() {
      Optional<RolesDontExistError> error = underTest.validate(of(Fixtures.ANALYST));

      assertThat(error).isEmpty();
    }

    @Test
    void approverRolePassed_returnsError() {
      Optional<RolesDontExistError> error = underTest.validate(of(Fixtures.APPROVER));

      assertThat(error).isNotEmpty();
    }

    @Test
    void analystAndMakerRolesPassed_returnsNoError() {
      Optional<RolesDontExistError> error =
          underTest.validate(of(Fixtures.ANALYST, Fixtures.MAKER));

      assertThat(error).isEmpty();
    }
  }
}