package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.common.protobuf.ObjectToMapConverter;

import com.mitchellbosecke.pebble.PebbleEngine;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class GenerateCommentUseCaseTest {

  private InMemoryCommentTemplateRepository commentTemplateRepository =
      new InMemoryCommentTemplateRepository();
  private CommentFacade facade =
      CommentTemplateFixture.inMemoryCommentFacade(commentTemplateRepository);

  private static final String PEBBLE1_TEMPLATE_NAME = "pebble1";
  private static final String PEBBLE2_TEMPLATE_NAME = "pebble2";

  GenerateCommentUseCaseTest() {
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

    AlertContext model = AlertContext.builder().alertId(alertId).build();
    var evaluated = facade.generateComment(PEBBLE1_TEMPLATE_NAME, model);

    assertThat(evaluated).isEqualTo(output);
  }

  @Test
  public void shouldGenerateCommentFromStruct() {
    String name = "Tomasz";
    var alertModel = randomAlertModel();
    String output = "alert/1 v1 1 l1";
    var evaluated = facade.generateComment(PEBBLE2_TEMPLATE_NAME, alertModel);
    assertThat(evaluated).isEqualTo(output);
  }

  @Test
  public void shouldConvertStructToMapOfObjects() {
    var alertModel = randomAlertModel();
    var alertModelMap = ObjectToMapConverter.convert(alertModel);
    assertThat(alertModelMap).isNotEmpty();
    assertThat(alertModelMap)
        .containsKeys("alertId", "commentInput", "recommendedAction", "matches");
  }

  private AlertContext randomAlertModel() {
    return AlertContext.builder()
        .alertId("alert/1")
        .commentInput(Map.of("o1", "v1", "o2", 1, "o3", List.of("l1", "l2")))
        .match(MatchContext.builder().matchId("match/1").build())
        .recommendedAction("NO_DECISION")
        .build();
  }
}
