package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;

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
class GenerateMatchCommentUseCaseTest {

  private final InMemoryCommentTemplateRepository repo =
      new InMemoryCommentTemplateRepository();

  private final CommentFacade facade =
      CommentTemplateFixture.inMemoryCommentFacade(repo);

  private static AlertContext randomAlertModel() {
    return AlertContext.builder()
        .alertId("alert-1")
        .commentInput(Map.of("o1", "v1", "o2", 1, "o3", List.of("l1", "l2")))
        .match(MatchContext.builder().matchId("match-1").build())
        .recommendedAction("NO_DECISION")
        .build();
  }

  @Test
  void shouldGenerateDefaultMatchComment() {
    var context = createAlertContext();

    var evaluated = facade.generateMatchComments("match", context.getMatches());

    for (var comment : evaluated.entrySet()) {
      assertThat(comment.getValue())
          .contains("NOTE: This is the default match comment template!")
          .contains("Match ID: 123")
          .contains("Agent Name: agents/dateAgent");
    }
  }

  @Test
  void shouldNotFailWhenPassUnsupportedType() {
    var alertModel = randomAlertModel();

    var evaluated = facade.generateMatchComments("some template", alertModel.getMatches());

    assertThat(evaluated).isEmpty();
  }

  @Test
  void shouldNotFailWhenPassBuggyTemplate() {
    repo.save(commentTemplate("buggy.ftl", readFile("freemarker/buggy.ftl")));
    var alertModel = randomAlertModel();

    var evaluated = facade.generateMatchComments("buggy", alertModel.getMatches());

    assertThat(evaluated).isEmpty();
  }

  @Nested
  class PebbleTest {

    @BeforeEach
    void setUp() {
      repo.save(commentTemplate("match1.peb", 0, readFile("pebble/match1.peb")));
      repo.save(commentTemplate("match1.peb", 1, readFile("pebble/match1.peb")));
      repo.save(commentTemplate("match2.peb", readFile("pebble/match2.peb")));
    }

    @Test
    void shouldGenerateComment() {
      var matchId = UUID.randomUUID().toString();
      var output = "This is template:" + matchId;
      var context = MatchContext.builder().matchId(matchId).build();

      var evaluated = facade.generateMatchComments("match1", List.of(context));

      assertThat(evaluated.get(matchId)).isEqualTo(output);
    }

    @Test
    void shouldGenerateCommentFromStruct() {
      var alertModel = randomAlertModel();
      var output = "match-1";
      var evaluated = facade.generateMatchComments("match2", alertModel.getMatches());
      assertThat(evaluated.get("match-1")).isEqualTo(output);
    }
  }
}
