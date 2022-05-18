package com.silenteight.payments.bridge.testing;

import com.icegreen.greenmail.util.ServerSetup;


public class GreenMailConfiguration {

  public static ServerSetup getServerStartup(int port) {
    final ServerSetup serverSetup = new ServerSetup(port, null, ServerSetup.PROTOCOL_SMTP);
    serverSetup.setServerStartupTimeout(5000L);
    serverSetup.setVerbose(true);
    return serverSetup;
  }
}
