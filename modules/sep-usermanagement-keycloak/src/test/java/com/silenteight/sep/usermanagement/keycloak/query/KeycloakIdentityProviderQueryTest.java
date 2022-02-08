package com.silenteight.sep.usermanagement.keycloak.query;


import com.silenteight.sep.usermanagement.api.identityprovider.dto.IdentityProviderDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.IdentityProvidersResource;
import org.keycloak.representations.idm.IdentityProviderRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakIdentityProviderQueryTest {

  private static final String IDP_ALIAS_1 = "Idp alias #1";
  private static final String IDP_ALIAS_2 = "Idp alias #2";
  private static final String IDP_DISPLAY_NAME_1 = "Idp displayName #1";
  private static final String IDP_DISPLAY_NAME_2 = "Idp displayName #2";
  private static final String IDP_INTERNAL_ID_1 = "5734acc7-7278-466c-b643-085a232243ad";
  private static final String IDP_INTERNAL_ID_2 = "eb80fb52-dadd-455a-af6f-916e88e7cfac";

  @InjectMocks
  private KeycloakIdentityProviderQuery underTest;

  @Mock
  IdentityProvidersResource identityProvidersResource;

  @Test
  void shouldReturnIdentityProviderDtoList() {
    //given
    when(identityProvidersResource.findAll())
        .thenReturn(createIdentityProviderRepresentationList());

    //when
    Collection<IdentityProviderDto> identityProviderDtosList = underTest.listAll();

    //then
    assertThat(identityProviderDtosList).hasSize(2);
    List<IdentityProviderDto> identityProviderDtoList = new ArrayList<>(identityProviderDtosList);
    assertThat(identityProviderDtoList.get(0).getAlias()).isEqualTo(IDP_ALIAS_1);
    assertThat(identityProviderDtoList.get(1).getAlias()).isEqualTo(IDP_ALIAS_2);
    assertThat(identityProviderDtoList.get(0).isEnabled()).isTrue();
    assertThat(identityProviderDtoList.get(1).isEnabled()).isFalse();
    assertThat(identityProviderDtoList.get(1).getDisplayName()).isEqualTo(IDP_ALIAS_2);
  }

  private static IdentityProviderRepresentation createIdentityProviderRepresentation(
      List<String> idpFields, boolean isEnabled) {

    IdentityProviderRepresentation identityProviderRepresentation =
        new IdentityProviderRepresentation();

    identityProviderRepresentation.setAlias(idpFields.get(0));
    identityProviderRepresentation.setDisplayName(idpFields.get(1));
    identityProviderRepresentation.setInternalId(idpFields.get(2));
    identityProviderRepresentation.setEnabled(isEnabled);
    return identityProviderRepresentation;
  }

  private static IdentityProviderRepresentation createIdProviderRepresentationWithOutDisplayName(
      List<String> idpFields, boolean isEnabled) {

    IdentityProviderRepresentation identityProviderRepresentation =
        new IdentityProviderRepresentation();

    identityProviderRepresentation.setAlias(idpFields.get(0));
    identityProviderRepresentation.setInternalId(idpFields.get(1));
    identityProviderRepresentation.setEnabled(isEnabled);
    return identityProviderRepresentation;
  }

  private static List<IdentityProviderRepresentation> createIdentityProviderRepresentationList() {
    List<IdentityProviderRepresentation> identityProviderRepresentationList = new ArrayList<>();
    IdentityProviderRepresentation idpRepresentation1 = createIdentityProviderRepresentation(
        of(IDP_ALIAS_1, IDP_DISPLAY_NAME_1, IDP_INTERNAL_ID_1),
        true);

    IdentityProviderRepresentation idpRepresentation2 =
        createIdProviderRepresentationWithOutDisplayName(
            of(IDP_ALIAS_2, IDP_INTERNAL_ID_2),
            false);

    identityProviderRepresentationList.add(idpRepresentation1);
    identityProviderRepresentationList.add(idpRepresentation2);
    return identityProviderRepresentationList;
  }
}
