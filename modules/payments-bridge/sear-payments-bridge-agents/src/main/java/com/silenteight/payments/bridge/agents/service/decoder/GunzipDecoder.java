package com.silenteight.payments.bridge.agents.service.decoder;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@RequiredArgsConstructor
class GunzipDecoder implements Decoder {

  private final String supportedExtension;

  @Override
  public boolean supports(@NonNull String url) {
    if (url.isBlank())
      throw new IllegalArgumentException("Input url cannot be blank");

    return url.trim().endsWith("." + supportedExtension);
  }

  @Override
  public InputStream decode(@NonNull InputStream inputStream) throws IOException {
    File file = Files.createTempFile(null, null).toFile();
    try (
        InputStream gzipStream = new GZIPInputStream(inputStream);
        OutputStream outputStream = Files.newOutputStream(file.toPath())) {
      int byteRead;
      while ((byteRead = gzipStream.read()) != -1) {
        outputStream.write(byteRead);
      }
      return Files.newInputStream(file.toPath());
    } catch (IOException e) {
      return rethrow(e);
    }
  }
}
