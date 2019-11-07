package com.silenteight.sens.webapp.user;

import org.apache.commons.codec.digest.DigestUtils;

class TokenEncoder {

  public String encode(String token) {
    return DigestUtils.sha256Hex(token);
  }
}
