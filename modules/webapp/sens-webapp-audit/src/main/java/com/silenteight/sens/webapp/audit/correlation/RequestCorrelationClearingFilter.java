package com.silenteight.sens.webapp.audit.correlation;

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
