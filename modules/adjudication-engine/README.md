# Adjudication Engine

[[_TOC_]]

## Development Setup

1. Clone Common Docker Infra repo: https://gitlab.silenteight.com/sens/common-docker-infrastructure.
2. Start docker services from Common Docker Infra repo (follow README in repo).
3. Start docker services from this repo using:

       docker-compose up -d

### Testing application with mocked dependencies

To test application with mocked dependencies activate following Spring Profiles:

    mockgovernance, mockagents, mockdatasource, rabbitdeclare

### Tracing gRPC calls

To enable tracing of gRPC calls, enable the `tracegrpc` Spring profile.

## Developing validation

The Adjudication Engine validates incoming gRPC requests.
To add validation to your requests, see the [Validation Guide](doc/validation-guide.md).

## Alert labels

To see available alert labels and it's values see [Alert labels and values list](doc/alert-labes-values.md)

## Datasource V1

To run AE using datasource API V1 use `datasourcev1` profile

# Comment templates

Basic versions of comment templates are added to `ae_comment_template` table in db by liquibase.

## Modification of comment templates

Modifying comment templates is described in [comment module](adjudication-engine-comments/comments.md)

## Testing comment templates

The new version of the comment template can be tested without engaging the whole SEAR
infrastructure. To do so follow the steps from `README.md` of `cli-commentator` project.

## Removing feature values

To remove feature values set up property:

```
ae:
  match-feature-value:
    not-cached:
      features:
        - "features/historicalRiskAccountNumberTP"
        - "features/historicalRiskAccountNumberFP"
```

These features will be periodically removed from Adjudication Engine database, in order for AE to ask
agents for responses for these features once again.

To determine how often these feature values are going to be removed set up:

```
ae:
  match-feature-value:
    not-cached:
      scheduler: * */10 * * * ?
```

In this example default value is presented.
