# Company name

- [Usage](#usage)
  - [Installing](#installing)
  - [Executing](#executing)
  - [Deploying](#deploying)
- [Configuration](#configuration)
  - [Config files](#config)
  - [Resources](#resources)
- [Implementation](#implementation)
  - [Parsing name](#parsing)
  - [Comparing](#comparing)
  - [Scores reduction](#scores-reduction)
  - [Pair solutions reduction](#pair-solutions-reduction)
  - [Agent exchange](#agent-exchange)
- [Tests](#tests)

<a name="usage"/>

# Usage

<a name="installing"/>

## Installing

Module needs python >= 3.7. Tested only on python 3.7.


* from PiPY (repo.silenteight.com/artifactory/api/pypi/pypi/simple):
  ```
  pip install company-name
  ```

* using jenkins artifacts:
  
  latest wheel available at https://jenkins.silenteight.com/view/all/job/company-name-agent/job/master/


* from source:

  `pip install .`

<a name="executing"/>

## Executing

### running agent after installation

To run agent installed in your environment, simply run
```
python -m company_name.main -c {configuration_dir}
```

```
usage: main.py [-h] [-c CONFIGURATION_DIR] [-v]

Company name agent

optional arguments:
  -h, --help            show this help message and exit
  -c CONFIGURATION_DIR, --configuration-dir CONFIGURATION_DIR
                        Path for configuration files
  -v, --verbose         Increase verbosity for debug purpose

```
Sample configuration is prepared in repository main folder under `config`.

For local run simply copy `config/application.local.yaml` into `config/application.yaml` - and prepare rabbitmq queues in your system (for example using https://gitlab.silenteight.com/sens/common-docker-infrastructure).

### running agent without installation

Installation is not needed, if zipfile for your python version / system is available.
Zipfile for latest python3.7 is available as artifact in jenkins (https://jenkins.silenteight.com/view/all/job/company-name-agent/job/master/)

```
python company_name-0.1.0.dev0.pyz -c {configuration_dir}
```

### using agent in your python code

To use agent in your python code, installation is necessary. After that, it depends what do you want to do.

For simple comparison of two names:
```
>>> from company_name import compare
>>> compare("The Korea Development bank, Seocho Branch", "KDB")

{'parenthesis_match': Score(status='NO_DATA', value=0, compared=((), ())), 'abbreviation': Score(status='OK', value=0.5, compared=(('The Korea Development bank',), ('KDB',))), 'fuzzy_on_base': Score(status='OK', value=0.17142857142857137, compared=(('Korea Development bank Seocho Branch',), ('KDB',))), 'fuzzy_on_suffix': Score(status='NO_DATA', value=0, compared=((), ())), 'fuzzy': Score(status='OK', value=0.1578947368421052, compared=(('The Korea Development bank Seocho Branch',), ('KDB',))), 'partial_fuzzy': Score(status='OK', value=0.33333333333333326, compared=(('The Korea Development bank Seocho Branch',), ('KDB',))), 'sorted_fuzzy': Score(status='OK', value=0.09302325581395351, compared=(('The Korea Development bank Seocho Branch',), ('KDB',))), 'legal_terms': Score(status='NO_DATA', value=0, compared=((), ())), 'tokenization': Score(status='OK', value=0.0, compared=(('The', 'Korea', 'Development', 'bank', 'Seocho', 'Branch'), ('KDB',))), 'absolute_tokenization': Score(status='OK', value=0, compared=(('The', 'Korea', 'Development', 'bank', 'Seocho', 'Branch'), ('KDB',))), 'blacklisted': Score(status='OK', value=0.0, compared=((), ())), 'country': Score(status='NO_DATA', value=0, compared=((), ())), 'phonetics_on_base': Score(status='OK', value=0.2857142857142857, compared=(('KRTFLPMNTPNKSXPRNX (Korea Development bank Seocho Branch)',), ('KTP (KDB)',))), 'phonetics': Score(status='OK', value=0.27272727272727265, compared=(('TKRTFLPMNTPNKSKPRNK (The Korea Development bank Seocho Branch)',), ('KTP (KDB)',))), 'potential_subsidiary': Score(status='NO_DATA', value=0.0, compared=((), ())), 'token_inclusion': Score(status='OK', value=0.0, compared=(('The', 'Korea', 'Development', 'bank', 'Seocho', 'Branch'), ('KDB',))), 'first_token': Score(status='OK', value=0.0, compared=(('Korea',), ('KDB',)))}
```

For final solution for two set of names:
```
>>> import pathlib
>>> from company_name import CompanyNameAgent

>>> agent = CompanyNameAgent()
>>> agent.resolve(['HP, INC'], ['HEWLETT-PACKARD COMPANY',  'HP CO.'])

Result(solution=<Solution.MATCH: 'MATCH'>, reason=Reason(partials=[PairResult(solution=<Solution.MATCH: 'MATCH'>, solution_probability=1, names=('HP, INC', 'HP CO.')), PairResult(solution=<Solution.MATCH: 'MATCH'>, solution_probability=0.8, names=('HP, INC', 'HEWLETT-PACKARD COMPANY'))]))
```

or, if you want to solution as simple python dict:
```
>>> from company_name.utils import simplify
>>> simplify(agent.resolve(['HP, INC'], ['HEWLETT-PACKARD COMPANY',  'HP CO.']))
{'solution': 'MATCH', 'reason': {'partials': [{'solution': 'MATCH', 'solution_probability': 1, 'names': ['HP, INC', 'HP CO.']}, {'solution': 'MATCH', 'solution_probability': 0.8, 'names': ['HP, INC', 'HEWLETT-PACKARD COMPANY']}]}}
```

<a name="deploying"/>

## Deploying
See `Jenkinsfile` for building, testing and deploying on Nomad
or `.gitlab-ci` for building, testing and deploying in PiPY. 

<a name="configuration"/>

# Configuration

<a name="config"/>

## Config files

Basic application configuration for running agent. Example configuration is stored in config directory.

* application.yaml - configuration for external integrations, such as agent exchange or data source

* reduction-rules.yaml - configuration for [scores reduction](#scores-reduction)
  
While starting agent, you can indicate config directory. Currently there is no way to use different configuration in one running agent.

<a name="resources"/>

## Resources

Static data used for comparison is currently stored in company_name/resources directory. It contains:
* joining_words.json - words such as "and"
* weak_words.json - words ignored in creating abbreviation or extracting legal terms or common words
* common_prefixes.json - commonly used at the begging of company name, such as "group"
* common_suffixes.json - commonly used at the end of company name, such as "manufacturing", "sales" or "enterprise"
* legal_terms.json - legal entity terms, with possible abbreviations and meaning
* countries.json - different names and abbreviations for countries

For now, there is no possibility to change this data for build / deployed agent.

<a name="implementation"/>

# Implementation

Given two sets of names, resolving it consists of few steps:
  * each name from each set is parsed
  * pair of names are created as cartesian product of sets
  * each pair is compared
  * each pair of comparison scores are reduced into single solution
  * pair solutions are reduced into one solution

<a name="parsing"/>

## Parsing name


```python
>>> from company_name.compare import parse_name
>>> print(parse_name("THE SOME NAME MANUFACTURING (PRIVATE) LIMITED (FRANCE) FUNDED IN 1917 (SNM)"))
THE SOME NAME MANUFACTURING (PRIVATE) LIMITED (FRANCE) FUNDED IN 1917 (SNM) (common_prefixes: THE, base: SOME NAME, common_suffixes: MANUFACTURING, legal: LIMITED PRIVATE, countries: FRANCE, parenthesis: SNM, other: FUNDED IN 1917)

```

Divides name into parts:
* common_prefixes
* base
* common_suffixes
* legal - also found in parentheses (if parenthesis contains only legal terms, otherwise it is parsed as other name in parenthesis section)
* countries - in parentheses only
* parenthesis - information found in parentheses other than countries and legals
* other - information found after legal terms

Assuming input is not empty, base shouldn't be empty either.

<a name="comparing"/>

## Comparing

```python
from company_name.compare import compare
compare("SNM INC.", "THE SOME NAME MANUFACTURING (PRIVATE) LIMITED (FRANCE) FUNDED IN 1917 (SNM)")
```

```
parenthesis_match                Score(status='OK', value=1.0, compared=((), ('SNM',)))
abbreviation                     Score(status='OK', value=1, compared=(('SNM',), ('THE SOME NAME MANUFACTURING',)))
fuzzy_on_base                    Score(status='OK', value=0.55, compared=(('SNM',), ('SOME NAME',)))
fuzzy_on_suffix                  Score(status='NO_ALERTED_PARTY_DATA', value=0.0, compared=((), ('MANUFACTURING',)))
fuzzy                            Score(status='OK', value=0.22, compared=(('SNM',), ('THE SOME NAME MANUFACTURING',)))
partial_fuzzy                    Score(status='OK', value=0.67, compared=(('SNM',), ('THE SOME NAME MANUFACTURING',)))
sorted_fuzzy                     Score(status='OK', value=0.13, compared=(('SNM',), ('THE SOME NAME MANUFACTURING',)))
legal_terms                      Score(status='OK', value=0, compared=(('INC.',), ('LIMITED', 'PRIVATE')))
tokenization                     Score(status='OK', value=0.0, compared=(('SNM',), ('THE', 'SOME', 'NAME', 'MANUFACTURING')))
absolute_tokenization            Score(status='OK', value=0, compared=(('SNM',), ('THE', 'SOME', 'NAME', 'MANUFACTURING')))
blacklisted                      Score(status='OK', value=0.0, compared=((), ()))
country                          Score(status='NO_ALERTED_PARTY_DATA', value=0.0, compared=((), ('FRANCE',)))
phonetics_on_base                Score(status='OK', value=0.86, compared=(('SNM (SNM)',), ('SMNM (SOME NAME)',)))
phonetics                        Score(status='OK', value=0.38, compared=(('SNM (SNM)',), ('TSMNMMNFKTRNK (THE SOME NAME MANUFACTURING)',)))
potential_subsidiary             Score(status='NO_DATA', value=0.0, compared=((), ()))
token_inclusion                  Score(status='OK', value=0.0, compared=(('SNM',), ('THE', 'SOME', 'NAME', 'MANUFACTURING')))
first_token                      Score(status='OK', value=0.0, compared=(('SNM',), ('SOME',)))
```

Most scores are computed on cleared names - lowercase and without national characters.

* **parenthesis_match**
  ```
  "HP, INC" vs "SOME NAME (HP)"
  Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=((), ('HP',)))
  ```
  possible values: 0 or 1
  
  compared: prefixes + base + suffixes with one of parenthesis value

  algorithm: exactly the same after clearing
  

* **abbreviation**
  ```
  "HP, INC" vs "HEWLETT-PACKARD COMPANY"
  Score(status=<ScoreStatus.OK: 'OK'>, value=1, compared=(('HP',), ('HEWLETT-PACKARD',)))
  ```
  possible values: between 0 and 1
  
  compared: base (assumed abbreviation) with prefixes + base + optionally suffixes + optionally legal (assumed source of abbreviation)
  
  algorithm: checking possible matches, traversing from left to right side of source and abbreviation simultaneously -
  acceptable non-obvious operations are:
    * using more letters from one word (but only consecutive ones)
    * skipping weak words
    * skipping suffixes and legals from the end, if they don't appear in abbreviation (but base words are obligatory)
    * digit as possible multiplication of next letter
    * treading hyphenated words as one or two

  
* **fuzzy_on_base**, **fuzzy_on_suffix**, **fuzzy**
  ```
  fuzzy                Score(status='OK', value=0.79, compared=(('Indonesia North Industries',), ('North Indonesia Industries',)))
  fuzzy_on_base        Score(status='OK', value=0.64, compared=(('Indonesia North',), ('North Indonesia',)))
  fuzzy_on_suffix      Score(status='OK', value=1.0, compared=(('Industries',), ('Industries',)))
  ```
  possible value: between 0 and 1
  
  compared: base / suffixes / prefixes + base + suffixes
  
  algorithm: ratio from rapidfuzz library with removed spaces (using levenshtein distance)

  
* **partial_fuzzy**, **sorted_fuzzy**
  ```
  fuzzy                Score(status='OK', value=0.79, compared=(('Indonesia North Industries',), ('North Indonesia Industries',)))
  partial_fuzzy        Score(status='OK', value=0.88, compared=(('Indonesia North Industries',), ('North Indonesia Industries',)))
  sorted_fuzzy         Score(status='OK', value=1.0, compared=(('Indonesia North Industries',), ('North Indonesia Industries',)))
  ```
  possible value: between 0 and 1
  
  compared: prefixes + base + suffixes
  
  algorithm: partial ratio / sorted ratio from rapidfuzz library with removed spaces (using levenshtein distance)

  
* **legal_terms**
  ```
  Score(status='OK', value=0.75, compared=(('private', 'limited'), ('ltd',)))
  ```
  possible value: between 0 and 1
  
  compared: legal
  
  algorithm: match combined legal terms meaning
    (or, if meaning not available in datasource, exact legal terms after expanding the abbreviation) -
    order is not taken into consideration
  

  
* **absolute_tokenization**
  ```
  Score(status='OK', value=3, compared=(('THE', 'EMBASSY', 'OF', 'THE', 'REPUBLIC', 'OF', 'ANGOLA', 'IN', 'THE', 'REPUBLIC', 'OF', 'KENYA'), ('EMBASSY', 'OF', 'THE', 'REPUBLIC', 'OF', 'ANGOLA')))
  ```
  possible value: non negative integers
  
  compared: prefixes + base + suffixes
  
  algorithm: number of common tokens, without weak words

  
* **tokenization**
  ```
  Score(status='OK', value=0.5, compared=(('THE', 'EMBASSY', 'OF', 'THE', 'REPUBLIC', 'OF', 'ANGOLA', 'IN', 'THE', 'REPUBLIC', 'OF', 'KENYA'), ('EMBASSY', 'OF', 'THE', 'REPUBLIC', 'OF', 'ANGOLA')))
  ```
  possible value: between 0 and 1
  
  compared: prefixes + base + suffixes
  
  algorithm: ratio of common tokens, without weak words

  
* **blacklisted**
  ```
  "Gazprom" vs "HP"
  Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=(('gazprom',), ()))
  ```
  
  possible value: 0 or 1
  
  compared: cleared input on alerted party side
  
  algorithm: search for defined words (needs exact word, for example "gazprom" blacklist won't work for "gazprombank")


* **country**
  ``` 
  "TOYOTA MOTOR FINANCE (CHINA)" vs "TOYOTA MOTOR FINANCE (THE PEOPLE'S REPUBLIC OF CHINA)"
  Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=(('CHINA',), ("THE PEOPLE'S REPUBLIC OF CHINA",)))
  ```
  
  possible value: between 0 and 1
  
  compared: countries
  
  algorithm: match countries without taking their order into consideration

* **phonetics_on_base**, **phonetics**
  ```
  "John Smith import & export" vs "Jon Smit imp & exp"
  phonetics_on_base:
    Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=(('ANSMT (John Smith)',), ('ANSMT (Jon Smit)',)))
  phonetics:
    Score(status=<ScoreStatus.OK: 'OK'>, value=0.83, compared=(('ANSMTMPRTKSPRT (John Smith import & export)',), ('ANSMTMPKSP (Jon Smit imp & exp)',)))
  ```
  possible value: between 0 and 1
  
  compared: base / prefixes + base + suffixes, original (not cleaned)
  
  algorithm: dmetaphone (from library phonetics, which do not support e.g. chinese or arabic)
  

* **token_inclusion**
  ```
  "The Walt Disney Company" vs "Disney"
   Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=(('The', 'Walt', 'Disney'), ('Disney',)))
  ```
  possible value: 0 or 1
  
  compared: prefixes + base + suffixes
  
  algorithm: if exactly one word on exactly one side, check if this token also appears at other side

* **first_token**
  ```
  "Allstate Identity Protection" vs "Allstate Heritage Life Insurance Company"
   Score(status='OK', value=1.0, compared=((Allstate',), ('Allstate',)))
  
  ```
  possible value: 0 or 1

  compared: first token of base

  algorithm: exactly the same after clearing



<a name="scores-reduction"/>

## Scores reduction

Reducing scores from comparison into single solution, for example MATCH / INCONCLUSIVE / NO_MATCH.

Reducing algorithm is defined in [config file](#config), reduction-rules.yaml. There are two type of rules:
  * basic feature rule
    ```yaml
    - feature: blacklisted
      threshold: 1
      solution: MATCH
    ```
    in which one score, if over defined threshold, determine solution. Solution probability can also be defined in rule, default is 1.
    

  * model rule
    ```yaml
      - source: model/tsaas-logistic-regression-2021.07.12.bin
        solutions:
          - solution: NO_MATCH
            label: NO_MATCH
            threshold: 0.9
          - solution: MATCH
            label: MATCH
            threshold: 0
    ```
    Source is always relative to config directory and contains a model - currently only sklearn models are supported.
    Model can be binary or multi-class. Solution probability is taken from model probability.
    
  All solutions used must be defined in `company_name/solution/solution.py` - but not all defined there needs to be used.
  If rules will not be comprehensive, the default solution is INCONCLUSIVE.

<a name="pair-solutions-reduction"/>

## Pair solutions reduction

List of pair solutions is sorted and the best solution is chosen.
Sorting is done by comparing solution (in order defined in `company_name/solution/solution.py`),
and if solution is the same, by comparing solution probability. Sample sorted pair solutions:
```python
[
    PairResult(solution=<Solution.MATCH: 'MATCH'>, solution_probability=1, names=('HP, INC', 'HP CO.')),
    PairResult(solution=<Solution.MATCH: 'MATCH'>, solution_probability=0.8, names=('HP, INC', 'HEWLETT-PACKARD COMPANY')),
    PairResult(solution=<Solution.NO_MATCH: 'NO_MATCH'>, solution_probability=0.9809527625612637, names=('HP, INC', 'GOOGLE')),
]
```

<a name="agent-exchange"/>

## Agent exchange

Integration with agent exchange nad data source service is implemented in asyncio loop, listening on queue defined in config file `application.yaml`.
Each resolving is then computed in separate process, where max number of processes is also defined in configuration.
Any error encountered should result in UNEXPECTED_ERROR solution.

<a name="tests"/>

# Tests

### Code quality

* flake8
* black
* isort

All code quality checks are included in tox (see `tox.ini`) and are checked in both gitlab and jenkins pipeline.

### Tests

Run tests with pytest:

`pytest tests`

Some tests are skipped - those are examples that needs a little more care to work correctly

For jenkins and gitlab runs, flag `--without-rabbitmq` is added - so tests with agent exchange
are not run, as it needs running rabbitmq instance.


