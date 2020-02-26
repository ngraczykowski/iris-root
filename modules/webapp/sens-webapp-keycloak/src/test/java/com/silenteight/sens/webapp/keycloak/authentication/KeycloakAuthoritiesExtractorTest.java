package com.silenteight.sens.webapp.keycloak.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakAuthoritiesExtractorTest {

  public static final AuthorityNameNormalizer NO_CHANGE_NORMALIZER = (name) -> name;

  private KeycloakAuthoritiesExtractor underTest;
  @Mock
  private KeycloakAuthenticationToken authToken;
  @Mock
  private KeycloakSecurityContext keycloakSecurityContext;
  @Mock
  private AccessToken accessToken;
  @Mock
  private Access realmAccess;

  @BeforeEach
  void setUp() {
    given(authToken.getCredentials())
        .willReturn(keycloakSecurityContext);

    given(keycloakSecurityContext.getToken())
        .willReturn(accessToken);

    given(accessToken.getRealmAccess())
        .willReturn(realmAccess);
  }

  @Test
  void givenExampleRolesAndAuthorities_noChangeNormalizer_mapsCorrect() {
    underTest = new KeycloakAuthoritiesExtractor(
        NO_CHANGE_NORMALIZER, NO_CHANGE_NORMALIZER);
    mockRoles(Fixtures.ANALYST_ROLE, Fixtures.AUDITOR_ROLE);
    mockAuthorities(
        Fixtures.VIEW_DECISION_TREES_AUTHORITY, Fixtures.MANAGE_DECISION_TREES_AUTHORITY);

    Set<GrantedAuthority> actual = underTest.extract(authToken);

    assertThat(actual).containsAll(Fixtures.allAuthorities());
  }

  @Test
  void givenPrefixAppendingAndSimpleNormalizers_mapsCorrectly() {
    AuthorityNameNormalizer prefixAppendingNormalizer = (name) -> "ROLE_" + name;
    underTest = new KeycloakAuthoritiesExtractor(
        prefixAppendingNormalizer, NO_CHANGE_NORMALIZER);
    mockRoles(Fixtures.ANALYST_ROLE, Fixtures.AUDITOR_ROLE);
    mockAuthorities(
        Fixtures.VIEW_DECISION_TREES_AUTHORITY, Fixtures.MANAGE_DECISION_TREES_AUTHORITY);

    Set<GrantedAuthority> actual = underTest.extract(authToken);

    assertThat(actual)
        .extracting(GrantedAuthority::getAuthority)
        .containsAll(Set.of(
            "ROLE_" + Fixtures.ANALYST_ROLE,
            "ROLE_" + Fixtures.AUDITOR_ROLE,
            Fixtures.VIEW_DECISION_TREES_AUTHORITY,
            Fixtures.MANAGE_DECISION_TREES_AUTHORITY
        ));
  }

  private void mockAuthorities(String... authorityNames) {
    Set<GrantedAuthority> authorities = Stream.of(authorityNames)
        .map(SimpleGrantedAuthority::new)
        .collect(toSet());

    given(authToken.getAuthorities()).willReturn(authorities);
  }

  private void mockRoles(String... roles) {
    given(realmAccess.getRoles()).willReturn(Set.of(roles));
  }

  private static class Fixtures {

    static final String ANALYST_ROLE = "Analyst";
    static final String AUDITOR_ROLE = "Auditor";

    static final String VIEW_DECISION_TREES_AUTHORITY = "view-decision-trees";
    static final String MANAGE_DECISION_TREES_AUTHORITY = "manage-decision-trees";

    static Set<GrantedAuthority> allAuthorities() {
      return Stream.of(
          ANALYST_ROLE,
          AUDITOR_ROLE,
          VIEW_DECISION_TREES_AUTHORITY,
          MANAGE_DECISION_TREES_AUTHORITY
      ).map(SimpleGrantedAuthority::new).collect(toSet());
    }
  }
}
