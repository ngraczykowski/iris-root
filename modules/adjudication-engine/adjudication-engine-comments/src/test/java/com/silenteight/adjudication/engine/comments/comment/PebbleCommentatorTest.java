package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.domain.AlertTemplateModel;
import com.silenteight.adjudication.engine.comments.domain.MatchTemplateModel;
import com.silenteight.adjudication.engine.common.protobuf.ProtoStructConverter;

import com.mitchellbosecke.pebble.PebbleEngine;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class PebbleCommentatorTest {

  private PebbleCommentator commentator;
  private ProtoStructConverter converter;


  private static final String PEBBLE1_TEMPLATE_NAME = "pebble1";
  private static final String PEBBLE2_TEMPLATE_NAME = "pebble2";

  PebbleCommentatorTest() {
    this.converter = new ProtoStructConverter();
    var engine = new PebbleEngine.Builder()
        .loader(new InMemoryPebbleDbLoader(
                Map.of(PEBBLE1_TEMPLATE_NAME, TestUtils.readFile("pebble1.tmpl"),
                    PEBBLE2_TEMPLATE_NAME, TestUtils.readFile("pebble2.tmpl"))
            )
        )
        .build();
    this.commentator = new PebbleCommentator(engine);
  }

  @Test
  public void shouldGenerateComment() {
    String name = "Tomasz";
    String output = "This is template:" + name;
    var evaluated = commentator.evaluate(PEBBLE1_TEMPLATE_NAME, Map.of("name", name));
    assertThat(evaluated).isEqualTo(output);
  }

  @Test
  public void shouldConvertStructToMapOfObjects() {
    var alertModel = randomAlertModel();
    var alertModelMap = converter.convert(alertModel);
    assertThat(alertModelMap).isNotEmpty();
    assertThat(alertModelMap)
        .containsKeys("alertId", "commentInput", "recommendedAction", "matches");
  }

  @Test
  public void shouldGenerateCommentFromStruct() {
    String name = "Tomasz";
    var alertModel = randomAlertModel();
    var alertModelMap = converter.convert(alertModel);
    String output = "alert/1 v1 1 l1";
    var evaluated = commentator.evaluate(PEBBLE2_TEMPLATE_NAME, alertModelMap);

    assertThat(evaluated).isEqualTo(output);
  }

  private AlertTemplateModel randomAlertModel() {
    return AlertTemplateModel.builder()
        .alertId("alert/1")
        .commentInput(Map.of("o1", "v1", "o2", 1, "o3", List.of("l1","l2")))
        .match(MatchTemplateModel.builder().matchId("match/1").build())
        .recommendedAction("NO_DECISION")
        .build();
  }
}
