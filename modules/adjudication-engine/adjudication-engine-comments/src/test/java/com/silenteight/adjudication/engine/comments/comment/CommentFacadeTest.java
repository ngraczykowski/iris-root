package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.domain.AlertTemplateModel;
import com.silenteight.adjudication.engine.comments.domain.MatchTemplateModel;
import com.silenteight.adjudication.engine.common.protobuf.ProtoStructConverter;

import com.mitchellbosecke.pebble.PebbleEngine;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class CommentFacadeTest {

  private ProtoStructConverter converter;
  private InMemoryCommentTemplateRepository commentTemplateRepository =
      new InMemoryCommentTemplateRepository();
  private CommentFacade facade =
      CommentTemplateFixture.inMemoryCommentFacade(commentTemplateRepository);

  private static final String PEBBLE1_TEMPLATE_NAME = "pebble1";
  private static final String PEBBLE2_TEMPLATE_NAME = "pebble2";

  CommentFacadeTest() {
    this.converter = new ProtoStructConverter();
    var engine = new PebbleEngine.Builder()
        .cacheActive(true)
        .loader(new CommentTemplateLoader(commentTemplateRepository))
        .build();
    commentTemplateRepository.save(CommentTemplate
        .builder()
        .revision(0)
        .templateName(PEBBLE1_TEMPLATE_NAME)
        .template(TestUtils.readFile("pebble1.tmpl"))
        .build());
    commentTemplateRepository.save(CommentTemplate
        .builder()
        .revision(1)
        .templateName(PEBBLE1_TEMPLATE_NAME)
        .template(TestUtils.readFile("pebble1.tmpl"))
        .build());
    commentTemplateRepository.save(CommentTemplate
        .builder()
        .revision(0)
        .templateName(PEBBLE2_TEMPLATE_NAME)
        .template(TestUtils.readFile("pebble2.tmpl"))
        .build());
  }

  @Test
  public void shouldGenerateComment() {
    String alertId = UUID.randomUUID().toString();
    String output = "This is template:" + alertId;

    AlertTemplateModel model = AlertTemplateModel.builder().alertId(alertId).build();
    var evaluated = facade.generate(PEBBLE1_TEMPLATE_NAME, model);

    assertThat(evaluated).isEqualTo(output);
  }

  @Test
  public void shouldGenerateCommentFromStruct() {
    String name = "Tomasz";
    var alertModel = randomAlertModel();
    String output = "alert/1 v1 1 l1";
    var evaluated = facade.generate(PEBBLE2_TEMPLATE_NAME, alertModel);
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

  private AlertTemplateModel randomAlertModel() {
    return AlertTemplateModel.builder()
        .alertId("alert/1")
        .commentInput(Map.of("o1", "v1", "o2", 1, "o3", List.of("l1", "l2")))
        .match(MatchTemplateModel.builder().matchId("match/1").build())
        .recommendedAction("NO_DECISION")
        .build();
  }
}
