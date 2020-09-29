package com.silenteight.sep.usermanagement.keycloak.remove;

import com.silenteight.sep.usermanagement.keycloak.retrieval.KeycloakUserRetriever;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UserResource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserRemoverTest {

  @Mock
  private KeycloakUserRetriever keycloakUserRetriever;

  @Mock
  private UserResource userResource;

  @InjectMocks
  private KeycloakUserRemover keycloakUserRemover;

  @Test
  void removeUser() {
    String username = "userABC";
    when(keycloakUserRetriever.retrieve(username)).thenReturn(userResource);

    keycloakUserRemover.remove(username);

    verify(userResource).remove();
  }
}
