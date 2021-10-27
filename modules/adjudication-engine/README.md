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
