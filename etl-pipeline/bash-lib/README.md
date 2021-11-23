# Bash Lib

Bash Lib is commonplace for utility bash scripts and functions.

Files in this repository can be shared between different projects.

## Usage

The recommended way is to include scripts as Git Submodule.

### Add as Git Submodule

Git Submodule should be added to the root directory of the project.

    git submodule add -b master ../../se/bash-lib.git

### Upgrade

Upgrade Git Submodule to the latest version using:

    git submodule update --rebase --remote

and push changes.

**NOTE:** If contents of bash-lib submodule is not visible then you may need to initialize it first.

    git submodule init

and then rebase as above.
 
### Clone project with Git Submodules

    git clone --recurse-submodules <project url>

### GitLab CI

    variables:
      GIT_SUBMODULE_STRATEGY: recursive

### Suggested usage

Take is as a template for your script:

```.env
#!/usr/bin/env bash

# Take the below if your bash script is in main project directory
bl_projdir="$(cd -- "$(dirname -- "${0}")" && pwd)"
# or this if it is in the subdirectory (i.e. bin, scripts, etc.)
bl_projdir="$(cd -- "$(dirname -- "${0}")" && cd .. && pwd)"

# Source our utils from bash-lib
source $bl_projdir/bash-lib/util.sh

# Put your code below
```
