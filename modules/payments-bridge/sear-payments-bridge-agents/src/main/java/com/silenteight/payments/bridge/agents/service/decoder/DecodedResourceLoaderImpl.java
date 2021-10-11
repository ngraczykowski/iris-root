package com.silenteight.payments.bridge.agents.service.decoder;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@RequiredArgsConstructor
class DecodedResourceLoaderImpl
    implements DecodedResourceLoader {

  private final Decoder decoder;
  private final ResourceLoader resourceLoaderToDelegate;

  @Override
  public @NotNull Resource getResource(@NotNull String location) {

    Resource resource = resourceLoaderToDelegate.getResource(location);
    try {
      if (decoder.supports(location)) {
        return new InputStreamResource(decoder.decode(resource.getInputStream()));
      } else {
        return resource;
      }
    } catch (IOException e) {
      return rethrow(e);
    }
  }

  @Override
  public ClassLoader getClassLoader() {
    throw new NotImplementedException("This loader does not return classloader");
  }
}
