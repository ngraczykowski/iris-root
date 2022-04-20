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

The comment templates can be updated in 2 ways:
- directly in database (table `ae_comment_template`);
- indirectly by calling the rest API (endpoint `/rest/ae/comment/template`).

In both cases you have to remember to set proper values of:
- `templateName` - usually it is a file name of a template with extension `.ftl` or `.peb`;
- `template` - the content of the template file;
- `revision` - it is an index number indicating the version of a given `templateName`.

> NOTE: If there are 2 templates with the same `templateName` then the one with higher revision 
> will be used to generate comments.

An example http request updating comment template is available in:
`adjudication-engine/doc/comment/add-comment-template.http`

> NOTE: If the request sent to rest API (endpoint `/rest/ae/comment/template`) will have the same
> `templateName` as an existing one in database then the next available `revision` will be used
> for that request.

## Testing comment templates

The new version of the comment template can be tested without engaging the whole SEAR
infrastructure. To do so follow the steps from `README.md` of `cli-commentator` project.
