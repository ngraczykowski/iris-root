package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.kernel.domain.DecisionTreeId;
import com.silenteight.sens.webapp.security.acl.dto.AccessListRequest;
import com.silenteight.sens.webapp.security.acl.dto.UserPermissionRequest;
import com.silenteight.sens.webapp.security.acl.exception.UserPermissionAlreadyGrantedException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;

import static com.silenteight.sens.webapp.kernel.security.SensPermission.DECISION_TREE_CHANGE;
import static com.silenteight.sens.webapp.kernel.security.SensPermission.DECISION_TREE_VIEW;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccessManagementServiceTest {

  private static final long DECISION_TREE_ID = 1L;
  private static final long USER_ID = 2L;

  @Mock
  private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy;
  @Mock
  private MutableAclService aclService;
  @Mock
  private PermissionFactory permissionFactory;
  @Mock
  private Authentication authentication;

  private AccessManagementService accessManagementService;

  @Before
  public void setUp() {
    doReturn(true).when(authentication).isAuthenticated();
    doReturn(String.valueOf(USER_ID)).when(authentication).getPrincipal();
    doReturn(asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
        .when(authentication)
        .getAuthorities();
    SecurityContextHolder.getContext().setAuthentication(authentication);

    accessManagementService = new AccessManagementService(
        objectIdentityRetrievalStrategy, aclService, permissionFactory);
  }

  @After
  public void tearDown() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  @Test
  public void givenAccessListRequest_createAccessList() {
    //given
    AccessListRequest request = makeAccessListRequest();
    ObjectIdentity identity = makeObjectIdentity();
    doReturn(identity).when(objectIdentityRetrievalStrategy).getObjectIdentity(makeDomainObject());

    //when
    accessManagementService.createAccessList(request);

    //then
    verify(aclService, times(1)).createAcl(identity);
  }

  @Test
  public void givenAccessListRequest_deleteAccessList() {
    //given
    AccessListRequest request = makeAccessListRequest();
    ObjectIdentity identity = makeObjectIdentity();
    doReturn(identity).when(objectIdentityRetrievalStrategy).getObjectIdentity(makeDomainObject());

    //when
    accessManagementService.deleteAccessList(request);

    //then
    verify(aclService, times(1)).deleteAcl(identity, true);
  }

  @Test
  public void givenUserPermissionRequest_grantAccess() {
    //given
    UserPermissionRequest request = makeUserPermissionRequest();
    ObjectIdentity identity = makeObjectIdentity();
    MutableAcl acl = makeAcl(identity);
    mockAccessOperations(identity, acl);

    //when
    accessManagementService.grantAccess(request);

    //then
    verify(aclService, times(1)).updateAcl(acl);
  }

  @Test
  public void givenUserPermissionRequestTwice_throwUserPermissionAlreadyGrantedException() {
    //given
    UserPermissionRequest request = makeUserPermissionRequest();
    ObjectIdentity identity = makeObjectIdentity();
    MutableAcl acl = makeAcl(identity);
    mockAccessOperations(identity, acl);

    //when
    accessManagementService.grantAccess(request);

    //then
    assertThatThrownBy(() -> accessManagementService.grantAccess(request))
        .isInstanceOf(UserPermissionAlreadyGrantedException.class);
  }

  @Test
  public void givenUserPermissionRequest_revokeAccess() {
    //given
    UserPermissionRequest request = makeUserPermissionRequest();
    ObjectIdentity identity = makeObjectIdentity();
    MutableAcl acl = makeAcl(identity);
    mockAccessOperations(identity, acl);

    //when
    accessManagementService.revokeAccess(request);

    //then
    verify(aclService, times(1)).updateAcl(acl);
  }

  private void mockAccessOperations(ObjectIdentity identity, MutableAcl acl) {
    AccessPermission viewPermission = new AccessPermission(DECISION_TREE_VIEW);
    AccessPermission changePermission = new AccessPermission(DECISION_TREE_CHANGE);
    doReturn(identity).when(objectIdentityRetrievalStrategy).getObjectIdentity(makeDomainObject());
    doReturn(acl).when(aclService).readAclById(identity);
    doReturn(viewPermission).when(permissionFactory).buildFromName(DECISION_TREE_VIEW.name());
    doReturn(changePermission).when(permissionFactory).buildFromName(DECISION_TREE_CHANGE.name());
  }

  private AccessListRequest makeAccessListRequest() {
    return new AccessListRequest(makeDomainObject());
  }

  private UserPermissionRequest makeUserPermissionRequest() {
    return new UserPermissionRequest(
        DecisionTreeId.of(DECISION_TREE_ID),
        USER_ID,
        "ROLE_ADMIN",
        new HashSet<>(asList(DECISION_TREE_VIEW, DECISION_TREE_CHANGE)));
  }

  private ObjectIdentity makeObjectIdentity() {
    return new ObjectIdentityImpl(
        DecisionTreeId.class.getName(), String.valueOf(DECISION_TREE_ID));
  }

  private Object makeDomainObject() {
    return DecisionTreeId.of(DECISION_TREE_ID);
  }

  private MutableAcl makeAcl(ObjectIdentity objectIdentity) {
    return new AclImpl(
        objectIdentity,
        objectIdentity.getIdentifier(),
        new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN")),
        new ConsoleAuditLogger());
  }
}
