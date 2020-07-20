package com.silenteight.sens.auth.authorization;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class AuthorizationFilter extends GenericFilterBean {

  private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

  private final List<PathProperties> paths;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (request instanceof HttpServletRequest && isNotAuthorized((HttpServletRequest) request)) {
      ((HttpServletResponse) response).sendError(
          HttpServletResponse.SC_UNAUTHORIZED, "User is not authorized");
      return;
    }

    chain.doFilter(request, response);
  }

  private boolean isNotAuthorized(HttpServletRequest request) {
    List<String> supportedRoles = getRolesAuthorizedForRequest(request);
    List<String> userRoles = getUserRoles();

    return supportedRoles
        .stream()
        .noneMatch(userRoles::contains);
  }

  private static List<String> getUserRoles() {
    Authentication authentication =
        SecurityContextHolder
            .getContext()
            .getAuthentication();
    return authentication != null
           ? authentication
               .getAuthorities()
               .stream()
               .map(GrantedAuthority::getAuthority)
               .collect(toList())
           : emptyList();
  }

  private List<String> getRolesAuthorizedForRequest(HttpServletRequest request) {
    String requestPath = getRequestPath(request);
    return paths
        .stream()
        .filter(p -> p.getRequestPath().equals(requestPath)).findFirst()
        .map(PathProperties::getRoles)
        .orElse(emptyList());
  }

  private static String getRequestPath(HttpServletRequest request) {
    return URL_PATH_HELPER.getPathWithinApplication(request);
  }
}
