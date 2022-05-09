package utils.datageneration;

import lombok.SneakyThrows;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class TemplateService {

  @SneakyThrows
  String template(String template, Map<String, String> parameters) {
    return StringSubstitutor.replace(template, parameters);
  }
}
