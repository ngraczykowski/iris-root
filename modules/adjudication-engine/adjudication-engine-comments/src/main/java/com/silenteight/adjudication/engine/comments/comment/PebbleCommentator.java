package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import com.mitchellbosecke.pebble.PebbleEngine;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PebbleCommentator {

  private final PebbleEngine pebbleEngine;

  @SneakyThrows
  public String evaluate(String template, Map<String, Object> input) {
    var compiledTemplate = pebbleEngine.getTemplate(template);
    Writer writer = new StringWriter();
    compiledTemplate.evaluate(writer, input);
    return writer.toString();
  }
}
