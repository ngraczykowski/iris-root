# ADR 0004: Using conventional commits

## Goal

Determine the format of commit messages in the project.

## Decision

The commit messages in this project shall follow the Conventional Commits specification:

- https://www.conventionalcommits.org/en/v1.0.0/

The elements allowed specifically for this project:

- `feat:` and `feat(<module>):`, e.g. `feat(datasource):`,
- `fix:` and `fix(<module>):`, e.g. `fix(ae):`,
- `perf:` and `perf(<module>):`, e.g., `perf(firco):`, for a code change that improves performance,
- `refactor:` and `refactor(<module>):`, e.g., `refactor(firco):`, for changes that neither fixes a bug nor adds a feature,
- `style:` and `style(<module>):`, e.g., `style(common):`, for changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc.),
- `test:` and `test(<module>):`, e.g., `test(common):`, for adding missing tests or correcting existing tests,
- exclamation mark for when the commit contains breaking change, e.g., `feat!:`, `feat(ae)!:`
- `docs:` for updates in documentation, ADRs, etc.,
- `config:` for changes in configuration, RabbitMQ definitions, etc.
- `build:` for changes in Gradle build files, dependencies updates, etc.
- `ci:` for changes in Jenkins configuration or build definition,

## Jira Task

If commit is connected to jira task then add Task id at the end of commit message

## Examples

### Example 1

    feat(firco): Add Firco controller error handling (ABC-123)
    
    The Firco Continuity Content Manager API requires the error to be in form
    that resembles SOAP 1.1 Fault.

### Example 2

    docs: Add ADR for using Conventional Commits specification
    
    The commit messages shall have an easy set of rules for creating
    an explicit commit history; which makes it easier to write automated
    tools on top of.

## FAQ

###  Do all my contributors need to use the Conventional Commits specification?

No! If you use a squash based workflow on Git, lead maintainers can clean up the commit messages as they’re merged—adding no workload to casual committers.
A common workflow for this is to have your git system automatically squash commits from a pull request and present a form for the lead maintainer to enter the proper git commit message for the merge.
