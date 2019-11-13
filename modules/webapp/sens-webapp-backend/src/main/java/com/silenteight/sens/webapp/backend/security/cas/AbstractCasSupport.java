package com.silenteight.sens.webapp.backend.security.cas;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public abstract class AbstractCasSupport implements InitializingBean {

  @NonNull
  private final ServiceProperties serviceProperties;

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(this.serviceProperties.getService(),
                   "serviceProperties.getService() cannot be null.");

  }

  protected String createServiceUrl(HttpServletRequest request,
                                    HttpServletResponse response) {
    return CommonUtils.constructServiceUrl(request, response,
                                           this.serviceProperties.getService(), null,
                                           this.serviceProperties.getServiceParameter(),
                                           this.serviceProperties.getArtifactParameter(),
                                           true);
  }

  protected String createCasUrl(String casUrlPrefix, String serviceUrl) {
    return CommonUtils.constructRedirectUrl(
        casUrlPrefix,
        this.serviceProperties.getServiceParameter(),
        serviceUrl,
        this.serviceProperties.isSendRenew(),
        false);
  }
}
