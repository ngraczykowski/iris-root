package com.silenteight.warehouse.test.hsbcbridgeclient.usecases;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.util.Map;

@RequiredArgsConstructor
class TemplateService {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  ObjectNode template(Resource templateResource, Map<String, String> parameters) {
    String template = Files.readString(templateResource.getFile().toPath());
    String templated = StringSubstitutor.replace(template, parameters);
    return (ObjectNode) objectMapper.readTree(templated);
  }
}
