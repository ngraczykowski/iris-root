package com.silenteight.warehouse.common.elastic;

import lombok.NonNull;

import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.NamedXContentRegistry.Entry;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.plugins.SearchPlugin.QuerySpec;

import java.io.IOException;

import static java.util.List.of;
import static org.elasticsearch.common.xcontent.DeprecationHandler.THROW_UNSUPPORTED_OPERATION;
import static org.elasticsearch.common.xcontent.json.JsonXContent.jsonXContent;
import static org.elasticsearch.index.query.AbstractQueryBuilder.parseInnerQueryBuilder;

public class DlsQueryProcessor {

  @NonNull
  private final NamedXContentRegistry registry;

  public DlsQueryProcessor() {
    registry = createRegistry();
  }

  public String serialize(TermsQueryBuilder builder) {
    return builder.toString();
  }

  public TermsQueryBuilder deserialize(String query) {
    try {
      return tryParse(query);
    } catch (Exception e) {
      throw new DlsQueryParsingException(e);
    }
  }

  private TermsQueryBuilder tryParse(String query) throws IOException {
    var parser = jsonXContent.createParser(registry, THROW_UNSUPPORTED_OPERATION, query);
    return (TermsQueryBuilder) parseInnerQueryBuilder(parser);
  }

  private static NamedXContentRegistry createRegistry() {
    Entry registryEntry = asRegistryEntry(new QuerySpec<>(
        TermsQueryBuilder.NAME, TermsQueryBuilder::new, TermsQueryBuilder::fromXContent));

    return new NamedXContentRegistry(of(registryEntry));
  }

  private static NamedXContentRegistry.Entry asRegistryEntry(SearchPlugin.QuerySpec<?> spec) {
    return new NamedXContentRegistry.Entry(QueryBuilder.class, spec.getName(),
        (p, c) -> spec.getParser().fromXContent(p));
  }
}
