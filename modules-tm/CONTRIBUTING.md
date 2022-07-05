# Contributing to Transaction Monitoring  <!-- omit in toc -->

The following is a set of rules and guidelines for contributing to Transaction Monitoring. The rules are hard, and you MUST follow them, while guidelines are for guiding you in the right direction. Either way you SHOULD use your best judgement and engineering expertise.

#### Table of contents

1. [Conventional commits](#conventional-commits)
2. [Pre-commit](#pre-commit)
3. [Coding Style](#coding-style)
4. [Committing Code](#committing-code)
5. [E2E Testing](#e2e-testing)
6. [Deployment](#deployment)

## Conventional commits

[The Conventional Commits specification](https://www.conventionalcommits.org/en/v1.0.0/) is a lightweight convention on top of commit messages. It provides an easy set of rules for creating an explicit commit history; which makes it easier to write automated tools on top of.

The commit message should be structured as follows:

    <type>[optional scope]: <description>

    [optional body]

    [optional footer(s)]

The ADR introducing conventional commits to Iris [can be found here](docs/adr/0001-using_conventional_commits.md).

**Please use conventional commits when contributing to Iris!**

## Pre-commit

[Pre-commit](https://pre-commit.com/) is a framework for managing and maintaining multi-language pre-commit hooks.

**Please install and use pre-commit for all contributions to Iris!**

## Coding style

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

## Committing Code

The branch name should be one of the following:

- `[ticket-id]-[ticket-name]`, e.g. `TM-8-walking-skeleton-in-iris`
- `[initials]/[description]`, e.g. `msw/add-contributing-doc`

Jira allows one to create a good branch name using copy-paste of the command from Jira ticket.

## E2E Testing

TBD

## Deployment

TBD
