package com.silenteight.simulator.common.web.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IpAddressExtractor {

  public static String from(HttpServletRequest request) {
    String forwarderHeader = request.getHeader("X-Forwarded-For");
    if (forwarderHeader == null) {
      return request.getRemoteAddr();
    } else {
      // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
      return new StringTokenizer(forwarderHeader, ",").nextToken().trim();
    }
  }
}
