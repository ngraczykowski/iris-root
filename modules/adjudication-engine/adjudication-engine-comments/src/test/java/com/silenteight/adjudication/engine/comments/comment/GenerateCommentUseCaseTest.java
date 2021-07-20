package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.common.protobuf.ObjectToMapConverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.adjudication.engine.comments.comment.CommentTemplateFixture.commentTemplate;
import static com.silenteight.adjudication.engine.comments.comment.CommentTemplateFixture.createAlertContext;
import static com.silenteight.adjudication.engine.comments.comment.TestUtils.readFile;
import static org.assertj.core.api.Assertions.*;

@Slf4j
class GenerateCommentUseCaseTest {

  private final InMemoryCommentTemplateRepository repo =
      new InMemoryCommentTemplateRepository();

  private final CommentFacade facade =
      CommentTemplateFixture.inMemoryCommentFacade(repo);

  private static AlertContext randomAlertModel() {
    return AlertContext.builder()
        .alertId("alert/1")
        .commentInput(Map.of("o1", "v1", "o2", 1, "o3", List.of("l1", "l2")))
        .match(MatchContext.builder().matchId("match/1").build())
        .recommendedAction("NO_DECISION")
        .build();
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
  void shouldNotFailWhenPassUnsupportedType() {
    var alertModel = randomAlertModel();

    var evaluated = facade.generateComment("some template", alertModel);

    assertThat(evaluated).isEmpty();
  }

  @Test
  void shouldNotFailWhenPassBuggyTemplate() {
    repo.save(commentTemplate("buggy.ftl", readFile("freemarker/buggy.ftl")));
    var alertModel = randomAlertModel();

    var evaluated = facade.generateComment("buggy", alertModel);

    assertThat(evaluated).isEmpty();
  }

  @Test
  void shouldConvertStructToMapOfObjects() {
    var alertModel = randomAlertModel();
    var alertModelMap = ObjectToMapConverter.convert(alertModel);
    assertThat(alertModelMap)
        .isNotEmpty()
        .containsKeys("alertId", "commentInput", "recommendedAction", "matches");
  }

  @Nested
  class PebbleTest {

    @BeforeEach
    void setUp() {
      repo.save(commentTemplate("pebble1.peb", 0, readFile("pebble/pebble1.peb")));
      repo.save(commentTemplate("pebble1.peb", 1, readFile("pebble/pebble1.peb")));
      repo.save(commentTemplate("pebble2.peb", readFile("pebble/pebble2.peb")));
    }

    @Test
    void shouldGenerateComment() {
      var alertId = UUID.randomUUID().toString();
      var output = "This is template:" + alertId;
      var context = AlertContext.builder().alertId(alertId).build();

      var evaluated = facade.generateComment("pebble1", context);

      assertThat(evaluated).isEqualTo(output);
    }

    @Test
    void shouldGenerateCommentFromStruct() {
      var alertModel = randomAlertModel();
      var output = "alert/1 v1 1 l1";
      var evaluated = facade.generateComment("pebble2", alertModel);
      assertThat(evaluated).isEqualTo(output);
    }
  }

  @Nested
  class FreemarkerTest {

    @Test
    void shouldGenerateCommentFromFreemarkerTemplate() {
      repo.save(commentTemplate("deps/alert.ftl", readFile("freemarker/deps/alert.ftl")));
      repo.save(commentTemplate(
          "deps/nested/nested.ftl", readFile("freemarker/deps/nested/nested.ftl")));
      repo.save(commentTemplate("freemarker1.ftl", readFile("freemarker/freemarker1.ftl")));
      var context = createAlertContext();

      var evaluated = facade.generateComment("freemarker1", context);

      assertThat(evaluated).isEqualTo(""
          + "The Alert ID is: 2137\n"
          + "This is nested template.");
    }

    @Test
    void shouldUseSharedLib() {
      repo.save(commentTemplate("shared-lib.ftl", readFile("freemarker/shared-lib.ftl")));
      var context = createAlertContext();

      var evaluated = facade.generateComment("shared-lib", context);

      assertThat(evaluated).isEqualTo("Test shared lib (stringUtils.join): a, b.");
    }
  }

  @Nested
  class OrderTest {

    @Test
    void shouldUseFreemarkerTemplate() {
      var context = createAlertContext();

      repo.save(commentTemplate("main.peb", readFile("pebble/main.peb")));
      assertThat(facade.generateComment("main", context)).isEqualTo("This is pebble template.");

      repo.save(commentTemplate("main.ftl", readFile("freemarker/main.ftl")));
      assertThat(facade.generateComment("main", context)).isEqualTo("This is freemarker template.");
    }
  }
}
