package com.silenteight.payments.bridge.agents.service.decoder;

import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

interface Decoder {

  boolean supports(@NonNull String url);

  InputStream decode(@NonNull InputStream inputStream) throws IOException;
}
