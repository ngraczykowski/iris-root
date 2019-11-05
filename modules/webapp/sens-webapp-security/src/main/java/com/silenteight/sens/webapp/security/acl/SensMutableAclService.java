package com.silenteight.sens.webapp.security.acl;

import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;

import javax.sql.DataSource;

class SensMutableAclService extends JdbcMutableAclService implements PermissionRepository {

  private static final String ACL_OWNER_ID = "_ACL_OWNER";

  SensMutableAclService(DataSource dataSource, LookupStrategy lookupStrategy, AclCache aclCache) {
    super(dataSource, lookupStrategy, aclCache);
  }

  @Override
  public MutableAcl createAcl(ObjectIdentity objectIdentity) {
    Assert.notNull(objectIdentity, "Object Identity required");

    // Check this object identity hasn't already been persisted
    if (retrieveObjectIdentityPrimaryKey(objectIdentity) != null) {
      throw new AlreadyExistsException("Object identity '" + objectIdentity + "' already exists");
    }

    PrincipalSid sid = new PrincipalSid(ACL_OWNER_ID);

    // Create the acl_object_identity row
    createObjectIdentity(objectIdentity, sid);

    // Retrieve the ACL via superclass (ensures cache registration, proper retrieval
    // etc)
    Acl acl = readAclById(objectIdentity);
    Assert.isInstanceOf(MutableAcl.class, acl, "MutableAcl should be been returned");

    return (MutableAcl) acl;
  }
}
