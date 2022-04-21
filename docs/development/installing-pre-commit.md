# Installing pre-commit

## Linux

The TL;DR recipe for setting up using that is the following (on modern Linux box):

```bash
# Standard directory for local binaries
mkdir -p ~/.local/bin

# Install pre-commit
rm -rf ~/.venv/pre-commit
python3 -m venv ~/.venv/pre-commit
~/.venv/pre-commit/bin/python3 -m pip install --upgrade pip
~/.venv/pre-commit/bin/pip3 install --upgrade setuptools wheel
~/.venv/pre-commit/bin/pip3 install --upgrade pre-commit
bash -c 'for f in ~/.venv/pre-commit/bin/pre-commit* ; do \
ln -fs "$f" ~/.local/bin/$(basename "$f") ; done'

# Install the pre-commit hook to template directory
git config --global init.templateDir ~/.git-template
pre-commit init-templatedir -t pre-commit ~/.git-template
```

## MacOS

```bash
# Install brew if not exist
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install pre-commit
brew install pre-commit

# Install the pre-commit hook to template directory
git config --global init.templateDir ~/.git-template
pre-commit init-templatedir -t pre-commit ~/.git-template
```
