package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.TemplateEngineRegistry.TemplateNotFoundException;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.common.protobuf.ObjectToMapConverter;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.adjudication.engine.comments.comment.CommentTemplateFixture.createAlertContext;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class GenerateCommentUseCaseTest {

  private final InMemoryCommentTemplateRepository commentTemplateRepository =
      new InMemoryCommentTemplateRepository();

  private final CommentFacade facade =
      CommentTemplateFixture.inMemoryCommentFacade(commentTemplateRepository);

  private static final String PEBBLE1_TEMPLATE_NAME = "pebble1";
  private static final String PEBBLE2_TEMPLATE_NAME = "pebble2";

  GenerateCommentUseCaseTest() {
    commentTemplateRepository.save(CommentTemplate
        .builder()
        .revision(0)
        .templateName(PEBBLE1_TEMPLATE_NAME + ".peb")
        .template(TestUtils.readFile("pebble1.tmpl"))
        .build());
    commentTemplateRepository.save(CommentTemplate
        .builder()
        .revision(1)
        .templateName(PEBBLE1_TEMPLATE_NAME + ".peb")
        .template(TestUtils.readFile("pebble1.tmpl"))
        .build());
    commentTemplateRepository.save(CommentTemplate
        .builder()
        .revision(0)
        .templateName(PEBBLE2_TEMPLATE_NAME + ".peb")
        .template(TestUtils.readFile("pebble2.tmpl"))
        .build());
  }

  @Test
  void shouldGenerateDefaultComment() {
    var context = createAlertContext();

    var evaluated = facade.generateComment("alert", context);

    assertThat(evaluated)
        .contains("NOTE: This is the default alert comment template!")
        .contains("Alert ID: 2137")
        .contains("Match ID: 123")
        .contains("Agent Name: agents/dateAgent");
  }

  @Test
  void shouldGenerateComment() {
    var alertId = UUID.randomUUID().toString();
    var output = "This is template:" + alertId;
    var context = AlertContext.builder().alertId(alertId).build();

    var evaluated = facade.generateComment(PEBBLE1_TEMPLATE_NAME, context);

    assertThat(evaluated).isEqualToIgnoringNewLines(output);
  }

  @Test
  void shouldGenerateCommentFromStruct() {
    var alertModel = randomAlertModel();
    var output = "alert/1 v1 1 l1";
    var evaluated = facade.generateComment(PEBBLE2_TEMPLATE_NAME, alertModel);
    assertThat(evaluated).isEqualToIgnoringNewLines(output);
  }

  @Test
  void shouldConvertStructToMapOfObjects() {
    var alertModel = randomAlertModel();
    var alertModelMap = ObjectToMapConverter.convert(alertModel);
    assertThat(alertModelMap)
        .isNotEmpty()
        .containsKeys("alertId", "commentInput", "recommendedAction", "matches");
  }

  @Test
  void shouldFailWhenPassUnsupportedType() {
    var alertModel = randomAlertModel();

    assertThrows(
        TemplateNotFoundException.class,
        () -> facade.generateComment("some template", alertModel));
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
