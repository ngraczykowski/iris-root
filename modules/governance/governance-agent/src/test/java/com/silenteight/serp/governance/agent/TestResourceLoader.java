package com.silenteight.serp.governance.agent;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

import static java.util.Optional.ofNullable;

public class TestResourceLoader implements ResourceLoader {

  private final Resource resource;

  public TestResourceLoader(String json) {
    this.resource = ofNullable(json)
        .map(input -> new ByteArrayResource(input.getBytes()))
        .orElse(null);
  }

  public TestResourceLoader(ClassPathResource resource) {
    this.resource = resource;
  }

  @Override
  public Resource getResource(String location) {
    return resource;
  }

  @Override
  public ClassLoader getClassLoader() {
    return ClassUtils.getDefaultClassLoader();
  }
}
