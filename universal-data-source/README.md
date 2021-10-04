# Universal Data Source

The universal implementation of Data Source API for use with Adjudication Engine and agents.

The application consist of three parts:

- category,
- comment inputs,
- feature inputs (agent inputs).

## To run it

1. Clone Universal Data Source project https://gitlab.silenteight.com/sens/universal-data-source
2. Follow the UDS readme to run it
3. Start the database using `make up`
4. In Run configuration settings change 'Use class path module' to '
   universal-data-source.universal-data-source-app.main'
5. Run the Spring Application using as a main class `UniversalDataSourceApplication`.

## Command examples

Creating a new category:

```
grpc_cli call localhost:24805 silenteight.datasource.categories.api.v2.CategoryService/BatchCreateCategories "
            categories: {
                name: 'categories/crossmatch'
                display_name: 'Crossmatch Input'
                type: ENUMERATED
                allowed_values: 'YES'
                allowed_values: 'NO'
                multi_value: false
            }"
```
Response:
```
categories {
  name: "categories/crossmatch"
  display_name: "Crossmatch Input"
  type: ENUMERATED
  allowed_values: "YES"
  allowed_values: "NO"
}
```
Listing all categories

```
grpc_cli call localhost:24805 silenteight.datasource.categories.api.v2.CategoryService/ListCategories "{}"
```

Response:
```
categories {
  name: "categories/crossmatch"
  display_name: "Crossmatch Input"
  type: ENUMERATED
  allowed_values: "YES"
  allowed_values: "NO"
}
categories {
  name: "categories/oneliner"
  display_name: "One Liner Inputs"
  type: ENUMERATED
  allowed_values: "YES"
  allowed_values: "NO"
}
...

```

Creating a new category value:

```
grpc_cli call localhost:24805 silenteight.datasource.categories.api.v2.CategoryValueService/CreateCategoryValues "category: 'categories/82'category_values: {match: 'alerts/123/matches/123'single_value: 'YES'}"
```
Response:
```
created_category_values {
  name: "categories/crossmatch/value/387666"
  match: "alerts/123/matches/123"
}
```

Get category values:

```
grpc_cli call localhost:24805 silenteight.datasource.categories.api.v2.CategoryValueService/BatchGetMatchesCategoryValues "category_matches: {categories: 'categories/crossmatch', matches: 'alerts/123/matches/123'}"
```

Response:
```
category_values {
  name: "categories/crossmatch/value/387666"
  match: "alerts/123/matches/123"
  single_value: "YES"
}
```

Create comment input:

```
grpc_cli call localhost:24805 silenteight.datasource.comments.api.v2.CommentInputService/BatchCreateCommentInput "comment_inputs: {alert: 'alerts/123' alert_comment_input: {} match_comment_inputs: {match: 'alerts/123/matches/123' comment_input: {}}}"
```
Note: alert_comment_input and comment_input contain data in json format. In proto file this type
of data is represented by google.protobuf.Struct, with which grpc cli is having an issues.
This case can be tested though through other tools like for example grpc ui.

Response:
```
created_comment_inputs {
  name: "comment-inputs/1070"
  alert: "alerts/123"
}
```

Get comment inputs:

```
grpc_cli call localhost:24805 silenteight.datasource.comments.api.v2.CommentInputService/BatchGetAlertsCommentInputs "alerts: 'alerts/123'"
```

Response:
```
comment_inputs {
  name: "comment-inputs/5"
  alert: "c"
  alert_comment_input {
  }
}
```

Get name agent input:

```
grpc_cli call localhost:24805 silenteight.datasource.api.name.v1.NameInputService/BatchGetMatchNameInputs "matches: 'alerts/0/matches/1029' features: 'features/name'"
```

Response:
```
name_inputs {
  match: "alerts/123/matches/123"
  name_feature_inputs {
    feature: "features/name"
    alerted_party_names {
      name: "John"
    }
    watchlist_names {
      name: "Joe"
    }
  }
}
```

Creating agent inputs:

Creating inputs for all agent are done with AgentInputService/BatchCreateAgentInputs.

Json representation of agent inputs:
```
agent_inputs: [
  {
    "match": "alerts/<Alert ID>/matches/<Match ID>"
    "feature_inputs": [
      {
        "feature": "features/<Feature ID>"
        "agent_feature_input": {}
      }
    ]
  }
]
```
Field agent_feature_input contains agents input data, for example: NameFeatureInput or
LocationFeatureInput. Because of custom type of data put in a field of type google.protobuf.Any
issues occur while using tools like grpc url or grpc ui.














