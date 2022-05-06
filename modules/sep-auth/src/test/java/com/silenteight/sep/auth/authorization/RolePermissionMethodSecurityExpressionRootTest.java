package com.silenteight.sep.auth.authorization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolePermissionMethodSecurityExpressionRootTest {

  private static final String METHOD = "LIST_REASONING_BRANCHES";
  private static final String BUSINESS_OPERATOR_ROLE = "BUSINESS_OPERATOR";
  private static final String VIEW_REASONING_BRANCHES_PERMISSION = "VIEW_REASONING_BRANCHES";

  @Test
  void whenPermissionsNotConfigured_isNotAuthorized() {
    // given
    Authentication authentication = mock(Authentication.class);
    PermissionsByRoleProvider permissionsByRoleProvider = mock(PermissionsByRoleProvider.class);
    RolePermissionMethodSecurityExpressionRoot root =
        new RolePermissionMethodSecurityExpressionRoot(
            authentication, emptyMap(), permissionsByRoleProvider);

    // when
    boolean isAuthorized = root.isAuthorized(METHOD);

    // then
    assertThat(isAuthorized).isFalse();
  }

  @Test
  void whenMethodPermissionsAreEmpty_isNotAuthorized() {
    // given
    Authentication authentication = mockAuthentication(BUSINESS_OPERATOR_ROLE);
    Map<String, Set<String>> permissionsByMethod = Map.of(METHOD, emptySet());
    Map<String, Set<String>> permissionsByRole =
        Map.of(BUSINESS_OPERATOR_ROLE, singleton(VIEW_REASONING_BRANCHES_PERMISSION));
    PermissionsByRoleProvider permissionsByRoleProvider =
        mockPermissionsByRoleProvider(permissionsByRole);
    RolePermissionMethodSecurityExpressionRoot root =
        new RolePermissionMethodSecurityExpressionRoot(
            authentication, permissionsByMethod, permissionsByRoleProvider);

    // when
    boolean isAuthorized = root.isAuthorized(METHOD);

    // then
    assertThat(isAuthorized).isFalse();
  }

  @Test
  void whenUserRoleIsNotConfigured_isNotAuthorized() {
    // given
    Authentication authentication = mockAuthentication("OTHER_ROLE");
    Map<String, Set<String>> permissionsByMethod =
        Map.of(METHOD, singleton(VIEW_REASONING_BRANCHES_PERMISSION));
    Map<String, Set<String>> permissionsByRole =
        Map.of(BUSINESS_OPERATOR_ROLE, singleton(VIEW_REASONING_BRANCHES_PERMISSION));
    PermissionsByRoleProvider permissionsByRoleProvider =
        mockPermissionsByRoleProvider(permissionsByRole);
    RolePermissionMethodSecurityExpressionRoot root =
        new RolePermissionMethodSecurityExpressionRoot(
            authentication, permissionsByMethod, permissionsByRoleProvider);

    // when
    boolean isAuthorized = root.isAuthorized(METHOD);

    // then
    assertThat(isAuthorized).isFalse();
  }

  @Test
  void whenMethodIsNotConfigured_isNotAuthorized() {
    // given
    Authentication authentication = mock(Authentication.class);
    Map<String, Set<String>> permissionsByMethod =
        Map.of("OTHER_METHOD", singleton(VIEW_REASONING_BRANCHES_PERMISSION));
    PermissionsByRoleProvider permissionsByRoleProvider = mock(PermissionsByRoleProvider.class);
    RolePermissionMethodSecurityExpressionRoot root =
        new RolePermissionMethodSecurityExpressionRoot(
            authentication, permissionsByMethod, permissionsByRoleProvider);

    // when
    boolean isAuthorized = root.isAuthorized(METHOD);

    // then
    assertThat(isAuthorized).isFalse();
  }

  @Test
  void whenUserRoleHasDifferentPermissionsThanMethod_isNotAuthorized() {
    // given
    Authentication authentication = mockAuthentication(BUSINESS_OPERATOR_ROLE);
    Map<String, Set<String>> permissionsByMethod =
        Map.of(METHOD, singleton(VIEW_REASONING_BRANCHES_PERMISSION));
    Map<String, Set<String>> permissionsByRole =
        Map.of(BUSINESS_OPERATOR_ROLE, singleton("OTHER_PERMISSION"));
    PermissionsByRoleProvider permissionsByRoleProvider =
        mockPermissionsByRoleProvider(permissionsByRole);
    RolePermissionMethodSecurityExpressionRoot root =
        new RolePermissionMethodSecurityExpressionRoot(
            authentication, permissionsByMethod, permissionsByRoleProvider);

    // when
    boolean isAuthorized = root.isAuthorized(METHOD);

    // then
    assertThat(isAuthorized).isFalse();
  }

  @Test
  void whenRoleContainsPermissionSameAsMethod_isAuthorized() {
    // given
    Authentication authentication = mockAuthentication(BUSINESS_OPERATOR_ROLE);
    Map<String, Set<String>> permissionsByMethod =
        Map.of(METHOD, singleton(VIEW_REASONING_BRANCHES_PERMISSION));
    Map<String, Set<String>> permissionsByRole =
        Map.of(BUSINESS_OPERATOR_ROLE, singleton(VIEW_REASONING_BRANCHES_PERMISSION));
    PermissionsByRoleProvider permissionsByRoleProvider =
        mockPermissionsByRoleProvider(permissionsByRole);
    RolePermissionMethodSecurityExpressionRoot root =
        new RolePermissionMethodSecurityExpressionRoot(
            authentication, permissionsByMethod, permissionsByRoleProvider);

    // when
    boolean isAuthorized = root.isAuthorized(METHOD);

    // then
    assertThat(isAuthorized).isTrue();
  }

  private static PermissionsByRoleProvider mockPermissionsByRoleProvider(
      Map<String, Set<String>> permissionsByRole) {

    PermissionsByRoleProvider provider = mock(PermissionsByRoleProvider.class);
    doReturn(permissionsByRole).when(provider).provide();
    return provider;
  }

  private static Authentication mockAuthentication(String role) {
    Authentication authentication = mock(Authentication.class);
    doReturn(singletonList(new SimpleGrantedAuthority(role)))
        .when(authentication).getAuthorities();
    return authentication;
  }
}
