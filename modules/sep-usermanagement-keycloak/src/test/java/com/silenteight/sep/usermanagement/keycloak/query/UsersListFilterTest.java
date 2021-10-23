package com.silenteight.sep.usermanagement.keycloak.query;

import com.silenteight.sep.usermanagement.keycloak.query.role.RolesProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersListFilterTest {

  private static final String ATTRIBUTE_VALUE = "atValue";
  private static final String ATTRIBUTE_NAME = "atName";
  private static final String OTHER_VALUE = "OTHER";
  private static final String REQUIRED_ROLE = "REQUIRED_ROLE";
  private static final String OTHER_ROLE = "OTHER_ROLE";
  private static final String USER_ID = "1";
  @Mock
  private RolesProvider rolesProvider;
  private UsersListFilter underTest;

  @Nested
  class NoFilterConfiguration {

    @BeforeEach
    void setUp() {
      underTest = new UsersListFilter(
          new AttributeFilter(emptyList()), new RealmRoleFilter(emptyList(), rolesProvider));
    }

    @Test
    void returnTrueForAnyRequest() {
      assertThat(underTest.isVisible(new UserRepresentation())).isTrue();
    }
  }

  @Nested
  class AttributeFilterConfigured {

    @BeforeEach
    void setUp() {
      underTest = new UsersListFilter(
          new AttributeFilter(of(new Attribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE))),
          new RealmRoleFilter(emptyList(), rolesProvider));
    }

    @Test
    void returnTrueWhenUserHasAttribute() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setAttributes(Map.of(ATTRIBUTE_NAME, of(ATTRIBUTE_VALUE)));
      assertThat(underTest.isVisible(userRepresentation)).isTrue();
    }

    @Test
    void returnTrueWhenUserHasMultipleAttributes() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setAttributes(Map.of(ATTRIBUTE_NAME, of(ATTRIBUTE_VALUE, OTHER_VALUE)));
      assertThat(underTest.isVisible(userRepresentation)).isTrue();
    }

    @Test
    void returnFalseWhenAttributeMissing() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setAttributes(Map.of(ATTRIBUTE_NAME, of(OTHER_VALUE)));
      assertThat(underTest.isVisible(userRepresentation)).isFalse();
    }
  }

  @Nested
  class RealmRoleFilterConfigured {

    @BeforeEach
    void setUp() {
      underTest = new UsersListFilter(
          new AttributeFilter(emptyList()),
          new RealmRoleFilter(of(REQUIRED_ROLE), rolesProvider));
    }

    @Test
    void returnTrueWhenUserHasRole() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setId(USER_ID);
      when(rolesProvider.hasRoleInRealm(USER_ID, of(REQUIRED_ROLE))).thenReturn(true);
      assertThat(underTest.isVisible(userRepresentation)).isTrue();
    }

    @Test
    void returnTrueWhenRoleIsMissing() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setId(USER_ID);
      when(rolesProvider.hasRoleInRealm(USER_ID, of(REQUIRED_ROLE))).thenReturn(false);
      assertThat(underTest.isVisible(userRepresentation)).isFalse();
    }
  }

  @Nested
  class AllFiltersConfigured {

    @BeforeEach
    void setUp() {
      underTest = new UsersListFilter(
          new AttributeFilter(of(new Attribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE))),
          new RealmRoleFilter(of(REQUIRED_ROLE), rolesProvider));
    }

    @Test
    void returnTrueWhenUserHasAttribute() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setAttributes(Map.of(ATTRIBUTE_NAME, of(ATTRIBUTE_VALUE)));
      assertThat(underTest.isVisible(userRepresentation)).isTrue();
    }

    @Test
    void returnTrueWhenUserHasRole() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setId(USER_ID);
      when(rolesProvider.hasRoleInRealm(USER_ID, of(REQUIRED_ROLE))).thenReturn(true);
      assertThat(underTest.isVisible(userRepresentation)).isTrue();
    }

    @Test
    void returnTrueWhenRoleIsMissing() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setId(USER_ID);
      userRepresentation.setAttributes(Map.of(ATTRIBUTE_NAME, of(OTHER_VALUE)));
      when(rolesProvider.hasRoleInRealm(USER_ID, of(REQUIRED_ROLE))).thenReturn(false);
      assertThat(underTest.isVisible(userRepresentation)).isFalse();
    }
  }
}
