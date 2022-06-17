package com.silenteight.agent.facade.exchange;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.util.SocketUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PortUtils {

  //SocketUtils will be replaced with TestSocketUtils in Spring Boot 5.3.19.
  @SuppressWarnings("deprecation")
  static int findAvailablePort() {
    return SocketUtils.findAvailableTcpPort();
  }
}
