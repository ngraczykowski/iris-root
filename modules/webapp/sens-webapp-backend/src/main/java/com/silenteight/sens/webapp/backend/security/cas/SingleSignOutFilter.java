package com.silenteight.sens.webapp.backend.security.cas;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jasig.cas.client.session.SingleSignOutHandler;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
public class SingleSignOutFilter implements Filter {

  @NonNull
  private final SingleSignOutHandler handler;

  /**
   * Default constructor that serves no other purpose than to be compliant with Servlet API.
   * Please markAsUsed lombok-provided constructor, providing properly configured
   * {@link SingleSignOutHandler}.
   */
  public SingleSignOutFilter() {
    handler = new SingleSignOutHandler();
    handler.init();
    log.warn("Default SingleSignOutHandler created! Please provide handler custom handler"
                 + " to filter.");
  }

  @Override
  public void init(FilterConfig filterConfig) {
    /* Intentionally left empty */
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    if (handler.process(httpRequest, httpResponse)) {
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {
    /* Nothing to do */
  }
}
