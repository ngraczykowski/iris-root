# Sens WebApp Documentation

## Prerequisites

Generating docx documentation requires installed `pandoc`.
See installation instruction at https://pandoc.org/installing.html.
In case you don't have `pandoc` you can still generate documentation in `pdf`.

## Styling doc

Styles from `template.docx` file are used in generated `docx` file.

## Generate documentation

Run command:

    gw clean docs
    
It will generate documentation in `pdf` and `docx` (if `pandoc` is installed).

Documentation will be in `build/asciidoc` directory.

## Guidelines

### Basics

1. Input each sentence in a new line.
2. Use the present tense.

### Grammar

1. Use the `Grazie` plugin for IntelliJ Idea: https://plugins.jetbrains.com/plugin/12175-grazie.
2. Use the `Grammarly` plugin for the web browser.
After finishing writing documentation, copy Asciidoc text to `Grammarly` and correct your mistakes.

### Listening with command

Put the `$` character before the command to indicate it is a command to be executed by the user.
In the next line after the command, you can paste the command output.

Example:

    [listing,indent=0,subs="attributes+,+quotes"]
    ----
        $ cd _<installation-directory>_
        $ bin/serp --help
        command output...
    ----

### Mentioning command

When you mention command in headers or paragraphs, use only the command without path and without `$` character.

Example:

    The final configuration files are generated every time `serp start` or `serp reload` command is executed.

### References

- https://plan.io/blog/technical-documentation/
- https://en.wikipedia.org/wiki/Simplified_Technical_English
- https://plainlanguage.gov/
- http://asd-ste100.org/request.html
- https://www.sitepoint.com/writing-software-documentation/
- http://tom.preston-werner.com/2010/08/23/readme-driven-development.html
- https://www.writethedocs.org/guide/writing/beginners-guide-to-docs/
- https://blog.prototypr.io/software-documentation-types-and-best-practices-1726ca595c7f
- https://www.devteam.space/blog/software-documentation-6-best-practices-that-work/
- https://hackernoon.com/need-for-better-documentation-638e60f2d045
- https://www.altexsoft.com/blog/business/technical-documentation-in-software-development-types-best-practices-and-tools/
- https://grammar.yourdictionary.com/capitalization/rules-for-capitalization-in-titles.html
