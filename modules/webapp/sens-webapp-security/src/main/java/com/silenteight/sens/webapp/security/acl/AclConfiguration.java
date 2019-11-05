package com.silenteight.sens.webapp.security.acl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.model.SidRetrievalStrategy;

import java.beans.ConstructorProperties;
import java.util.List;
import javax.sql.DataSource;

import static java.util.Collections.emptyList;

@Configuration
@EnableCaching
class AclConfiguration {

  private static final String ACL_CACHE_NAME = "aclCache";
  private static final String POSTGRESQL_CLASS_IDENTITY_QUERY =
      "select currval(pg_get_serial_sequence('acl_class', 'id'))";
  private static final String POSTGRESQL_SID_IDENTITY_QUERY =
      "select currval(pg_get_serial_sequence('acl_sid', 'id'))";

  private final DataSource dataSource;
  private CacheManager cacheManager;

  private final PermissionGrantingStrategy permissionGrantingStrategy;
  private final AclAuthorizationStrategy aclAuthorizationStrategy;
  private final SidRetrievalStrategy sidRetrievalStrategy;
  private final AclClassIdUtilsInjector aclClassIdUtilsInjector;
  private final AclCache aclCache;

  @Value("#{'${spring.datasource.driver-class-name}'.equals('org.postgresql.Driver')}")
  private boolean isPostgres;

  private List<AbstractDomainObjectIdentityGenerator> objectIdentityGenerators = emptyList();

  @Autowired(required = false)
  public void setObjectIdentityGenerators(
      List<AbstractDomainObjectIdentityGenerator> objectIdentityGenerators) {
    this.objectIdentityGenerators = objectIdentityGenerators;
  }

  @Autowired(required = false)
  public void setCacheManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @Autowired(required = false)
  public void setConversionService(ConversionService conversionService) {
    aclClassIdUtilsInjector.setConversionService(conversionService);
  }

  @ConstructorProperties({ "dataSource" })
  public AclConfiguration(DataSource dataSource) {
    this.dataSource = dataSource;
    permissionGrantingStrategy = new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    aclAuthorizationStrategy = new SensAclAuthorizationStrategy();
    sidRetrievalStrategy = new UserAuthoritySidRetrievalStrategy();
    aclClassIdUtilsInjector = new AclClassIdUtilsInjector();
    aclCache = createAclCache();
  }

  private AclCache createAclCache() {
    Cache cache = cacheManager != null ? cacheManager.getCache(ACL_CACHE_NAME) : null;

    if (cache == null)
      cache = new ConcurrentMapCache(ACL_CACHE_NAME);

    return new SpringCacheBasedAclCache(
        cache, permissionGrantingStrategy, aclAuthorizationStrategy);
  }

  @Bean
  PermissionEvaluator aclPermissionEvaluator(
      MutableAclService aclService, PermissionFactory permissionFactory) {

    AclPermissionEvaluator evaluator = new SensAclPermissionEvaluator(aclService);
    evaluator.setObjectIdentityRetrievalStrategy(createObjectIdentityRetrievalStrategy());
    evaluator.setSidRetrievalStrategy(sidRetrievalStrategy);
    evaluator.setPermissionFactory(permissionFactory);
    return evaluator;
  }

  private CompositeObjectIdentityRetrievalStrategy createObjectIdentityRetrievalStrategy() {
    return new CompositeObjectIdentityRetrievalStrategy(objectIdentityGenerators);
  }

  @Bean
  SensMutableAclService aclService() {
    SensMutableAclService aclService =
        new SensMutableAclService(dataSource, lookupStrategy(), aclCache);
    aclService.setAclClassIdSupported(true);
    aclClassIdUtilsInjector.injectTo(aclService);

    if (isPostgres) {
      aclService.setClassIdentityQuery(POSTGRESQL_CLASS_IDENTITY_QUERY);
      aclService.setSidIdentityQuery(POSTGRESQL_SID_IDENTITY_QUERY);
    }

    return aclService;
  }

  private BasicLookupStrategy lookupStrategy() {
    BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(
        dataSource, aclCache, aclAuthorizationStrategy, permissionGrantingStrategy);
    lookupStrategy.setAclClassIdSupported(true);
    aclClassIdUtilsInjector.injectTo(lookupStrategy);
    return lookupStrategy;
  }

  @Bean
  AccessManagementService accessManagementService(
      MutableAclService aclService, PermissionFactory permissionFactory) {
    return new AccessManagementService(
        createObjectIdentityRetrievalStrategy(), aclService, permissionFactory);
  }

  @Bean
  PermissionFactory permissionFactory() {
    return new AccessPermissionFactory();
  }

  @Bean
  PermissionFinder permissionFinder(
      SensMutableAclService sensMutableAclService, PermissionEvaluator permissionEvaluator) {
    return new PermissionFinder(
        sensMutableAclService, permissionEvaluator);
  }

  @Bean
  AccessListFinder accessListFinder() {
    return new AccessListFinder(createObjectIdentityRetrievalStrategy(), lookupStrategy());
  }
}
