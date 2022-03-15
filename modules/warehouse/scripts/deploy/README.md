## How to set up Python Virtual Environment

Below you can skip if you did it before. 

1. Install **python 3.8+**

    for Ubuntu: ``sudo apt install python3``
    
    for MacOs: ``brew install python3`` 

2. Install **python 3.8+ Virtual Environment**

    for Ubuntu: ``sudo apt install python3.8-venv``

4. execute: ``pip install virtualenv`` or ``pip3 install virtualenv``

5. execute ``python3 -m virtualenv -h``

   if above command works it means that you have Python Virtual Environment Installed.

## Install Nomad Client

for Ubuntu: ``sudo apt install nomad``

for Mac: ``brew install hashicorp/tap/nomad``

## How to activate Virtual Environment

1. Open terminal and go to folder where you have your **deploy.py** script

2. execute: ``python3 -m venv path-to-deploy/venv``

    Folder ``venv`` should appear

3. Active your virtual environment: ``source venv/bin/activate``

**PS:**
if you use **PyCharm IDE** - Virtual Environment it is much easier to do, because IDE is doing it for you automatically.

## Install all necessary packages

Install all required packages: ``pip install -r requirements.txt`` and now you can run and develop a script.

## Script Description

``deploy.py`` - deployment Python script

``single_artifact.json`` - single configuration good for development purposes

Naming convention of files: ``artifacts_[client]_release_[number of release].json``

### Example of HSBC release 1.3.0:
``artifacts_hsbc_release_1.3.0.json`` - json to deploy whole HSCB environment on Nomad

## How to run it ?
1. Dry Run: ``python3 deploy.py <git-key> single_artifact.json --env dev --dry_run``
2. Hot run: ``python3 deploy.py <git-key> single_artifact.json --env dev``

replace ``<git-key>`` with your GitLab key

You can generate it on site: https://gitlab.silenteight.com/-/profile/personal_access_tokens

## How to remove venv ?
1. execute command ``deactivate``
2. and then remove ``venv`` folder

**PS:** ``venv`` folder added to ``.gitignore``
