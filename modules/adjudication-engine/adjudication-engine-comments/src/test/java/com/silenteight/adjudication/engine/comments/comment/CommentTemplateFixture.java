package com.silenteight.adjudication.engine.comments.comment;


import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.FeatureContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

class CommentTemplateFixture {

  private static final ConcurrentMap<String, AtomicInteger> TEMPLATE_REVISIONS =
      new ConcurrentHashMap<>();

  static CommentFacade inMemoryCommentFacade(InMemoryCommentTemplateRepository repository) {
    var pebbleConfiguration = new PebbleConfiguration();
    var loader = new PebbleCommentTemplateLoader(repository);
    var engine = pebbleConfiguration.pebbleEngine(loader);
    var templateEngine = new PebbleTemplateEngine(engine);

    var registry = new TemplateEngineRegistry(List.of(templateEngine));
    var useCase = new GenerateCommentUseCase(registry);

    return new CommentFacade(useCase);
  }

  public static CommentTemplate commentTemplate(String name, String payload) {
    var revision = TEMPLATE_REVISIONS.computeIfAbsent(name, s -> new AtomicInteger(1));
    return commentTemplate(name, revision.getAndIncrement(), payload);
  }

  public static CommentTemplate commentTemplate(
      String name, int revision, String payload) {

    return CommentTemplate.builder()
        .templateName(name)
        .revision(revision)
        .template(payload)
        .build();
  }

  public static AlertContext createAlertContext() {
    var commentInput = new HashMap<String, Object>();
    commentInput.put("comment", "input");
    return new AlertContext(
        "2137", commentInput, "recommended action",
        IntStream.range(1, 5).mapToObj(i -> createMatchContext()).collect(
            toList()));
  }

  static MatchContext createMatchContext() {
    var reason = new HashMap<String, Object>();
    reason.put("reason", "no_reason");

    var categories = new HashMap<String, String>();
    categories.put("key", "value");

    return MatchContext
        .builder()
        .matchId("123")
        .solution("NO_SOLUTION")
        .reason(reason)
        .categories(categories)
        .feature("features/dob", createFutureContext())
        .build();
  }

  static FeatureContext createFutureContext() {
    var reason = new HashMap<String, Object>();
    reason.put("reason", "no_reason");
    return FeatureContext
        .builder()
        .agentConfig("agents/dateAgent/versions/1.0.0/config/1")
        .solution("NO_SOLUTION")
        .reason(reason)
        .build();
  }
}
