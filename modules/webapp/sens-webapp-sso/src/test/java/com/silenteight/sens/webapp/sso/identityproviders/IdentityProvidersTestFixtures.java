package com.silenteight.sens.webapp.sso.identityproviders;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sep.usermanagement.api.dto.IdentityProviderDto;

import java.util.List;

import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IdentityProvidersTestFixtures {

  public static final String IDP_ALIAS_1 = "Idp alias #1";
  public static final String IDP_ALIAS_2 = "Idp alias #2";
  public static final String IDP_DISPLAY_NAME_1 = "Idp displayName #1";
  public static final String IDP_DISPLAY_NAME_2 = "Idp displayName #2";
  public static final String IDP_INTERNAL_ID_1 = "5734acc7-7278-466c-b643-085a232243ad";
  public static final String IDP_INTERNAL_ID_2 = "eb80fb52-dadd-455a-af6f-916e88e7cfac";

  public static final IdentityProviderDto IDENTITY_PROVIDER_DTO_1 =
      IdentityProviderDto.builder()
          .alias(IDP_ALIAS_1)
          .displayName(IDP_DISPLAY_NAME_1)
          .internalId(IDP_INTERNAL_ID_1)
          .enabled(true)
          .build();

  public static final IdentityProviderDto IDENTITY_PROVIDER_DTO_2 =
      IdentityProviderDto.builder()
          .alias(IDP_ALIAS_2)
          .displayName(IDP_DISPLAY_NAME_2)
          .internalId(IDP_INTERNAL_ID_2)
          .enabled(false)
          .build();

  public static final List<IdentityProviderDto> IDENTITY_PROVIDER_LIST =
      of(IDENTITY_PROVIDER_DTO_1, IDENTITY_PROVIDER_DTO_2);
}
