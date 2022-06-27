package com.silenteight.sens.webapp.audit.correlation;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;

import java.io.IOException;
import javax.servlet.*;

class RequestCorrelationClearingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    chain.doFilter(request, response);

    RequestCorrelation.remove();
  }
}
