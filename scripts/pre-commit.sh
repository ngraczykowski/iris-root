#!/usr/bin/env bash
changed_java_files=$(git diff --cached --name-only --diff-filter=ACMR | grep ".*java$" )
echo $changed_java_files

if [[ -n "$changed_java_files" ]]; then
    java \
    --add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED \
    -jar ~/.local/bin/google-java-format.jar --replace $changed_java_files
else
    echo No Java files changed!
fi

pre-commit run --show-diff-on-failure --color=always --all-files
git diff --quiet
if [[ $? -ne 0 ]]; then
    git checkout HEAD -B $CHANGE_BRANCH
    git commit -am 'chore: pre-commit fixes'
    git push --set-upstream origin $CHANGE_BRANCH
fi
