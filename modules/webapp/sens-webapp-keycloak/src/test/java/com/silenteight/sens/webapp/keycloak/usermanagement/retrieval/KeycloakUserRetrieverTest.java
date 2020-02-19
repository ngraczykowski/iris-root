package com.silenteight.sens.webapp.keycloak.usermanagement.retrieval;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.UserRepresentationFixtures.JOHN_SMITH;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserRetrieverTest {

  @InjectMocks
  private KeycloakUserRetriever underTest;

  @Mock
  private UsersResource usersResource;

  @Test
  void emptyUsersList_throwsUserNotFoundException() {
    // given
    when(usersResource.search(JOHN_SMITH.getUsername())).thenReturn(emptyList());

    // when
    Executable when = () -> underTest.retrieve(JOHN_SMITH.getUsername());

    // then
    assertThrows(KeycloakUserNotFoundException.class, when);
  }

  @Test
  void usersList_retrieveUserWithGivenUsername() {
    // given
    UserResource userResource = mock(UserResource.class);
    when(usersResource.search(JOHN_SMITH.getUsername())).thenReturn(singletonList(JOHN_SMITH));
    when(usersResource.get(JOHN_SMITH.getId())).thenReturn(userResource);
    when(userResource.toRepresentation()).thenReturn(JOHN_SMITH);

    // when
    UserResource result = underTest.retrieve(JOHN_SMITH.getUsername());

    // then
    assertThat(result.toRepresentation())
        .extracting(UserRepresentation::getUsername).isEqualTo(JOHN_SMITH.getUsername());
  }
}
