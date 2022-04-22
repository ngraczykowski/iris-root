# Organization name agent

- [Usage](#usage)
  - [Installing](#installing)
  - [Executing](#executing)
  - [Deploying](#deploying)
  - [Agent exchange](#agent-exchange)
  - [Grpc service](#grpc-service)
- [Configuration](#configuration)
  - [Config files](#config)
- [Implementation](#implementation)
  - [Name Parsing](#name-parsing)
  - [Comparing Names](#comparing)
  - [Solution Reduction](#solution-reduction)

- [Tests](#tests)

<a name="usage"/>

# Usage

<a name="installing"/>

## Installing

Package needs Python >= 3.7. Package and dependencies zipped in .pyz file needs exactly Python == 3.7

* from PiPY (repo.silenteight.com/artifactory/api/pypi/pypi/simple):

  `pip install company-name`

  *When installing using pip, please note that you will need to download or create a config, please check 
  [Configuration](#configuration)*


* from source:

  `pip install .`

<a name="executing"/>

## Executing

### running agent after installation

To run agent installed in your environment, simply run
```
python -m company_name.main [-h] [-c CONFIGURATION_DIR] [--grpc] [--agent-exchange] [-v]

Company name agent

optional arguments:
  -h, --help            show this help message and exit
  -c CONFIGURATION_DIR, --configuration-dir CONFIGURATION_DIR
                        Path for configuration files
  --grpc                Start grpc service
  --agent-exchange      Start agent exchange
  -v, --verbose         Increase verbosity for debug purpose

```
Sample configuration is prepared in repository main folder under `config`.

For local run simply copy `config/application.local.yaml` into `config/application.yaml` - and prepare rabbitmq queues in your system (for example using https://gitlab.silenteight.com/sens/common-docker-infrastructure).

### running agent without installation

Installation is not needed, if zipfile for your python version / system is available.
Zipfile for latest python3.7 is available as artifact in 

https://repo.silenteight.com:443/artifactory/dist-public/organization-name-agent/

```
python company_name-{VERSION}.pyz -c {configuration_dir} --grpc --agent-exchange
```

### using agent in your python code

To use agent in your python code, installation is necessary. After that, it depends on what do you want to do.

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
>>> from agent_base.utils import Config

>>> config = Config(configuration_dirs=(pathlib.Path("./config"),))
>>> agent = CompanyNameAgent(config)
>>> agent.resolve(['HP, INC'], ['HEWLETT-PACKARD COMPANY',  'HP CO.'])

Result(solution=<Solution.MATCH: 'MATCH'>, reason=Reason(results=[PairResult(solution=<Solution.MATCH: 'MATCH'>, solution_probability=1, alerted_party_name: 'HP, INC', watchlist_party_name: HP CO.'), PairResult(solution=<Solution.MATCH: 'MATCH'>, solution_probability=0.8, alerted_party_name='HP, INC', watchlist_party_name='HEWLETT-PACKARD COMPANY)]))
```

or, if you want to solution as simple python dict:
```
>>> from company_name.utils import simplify
>>> simplify(agent.resolve(['HP, INC'], ['HEWLETT-PACKARD COMPANY',  'HP CO.']))
{'solution': 'MATCH', 'reason': {'results': [{'solution': 'MATCH', 'solution_probability': 1, 'alerted_party_name': 'HP, INC', 'watchlist_party_name': HP CO.'}, {'solution': 'MATCH', 'solution_probability': 0.8, 'alerted_party_name': 'HP, INC', 'watchlist_party_name': 'HEWLETT-PACKARD COMPANY'}]}}
```

<a name="deploying"/>

## Deploying
See `Jenkinsfile` for building, testing and deploying on PYPI, repo.silenteight, Nomad

<a name="agent-exchange"/>

## Agent exchange

Using [agent exchange protobufs](https://gitlab.silenteight.com/ro/agents-api/-/tree/master/agents-api) in rabbitmq queues
and [name data source protobufs](https://gitlab.silenteight.com/sens/data-source-api/-/tree/master/data-source-api) in grpc requests to data source.

Possible solutions: MATCH, INCONCLUSIVE, NO_MATCH, NO_DATA and in case of unexpected error AGENT_ERROR or DATA_SOURCE_ERROR.

<a name="grpc-service"/>

## Grpc service

Using [protobufs for organization name agent](https://gitlab.silenteight.com/ro/agents-api/-/blob/master/protocol-agents/src/main/proto/silenteight/agent/organizationname/v1/api/organization_name_agent.proto).

Possible solutions: MATCH, INCONCLUSIVE, NO_MATCH, NO_DATA and in case of unexpected error AGENT_ERROR.

<a name="configuration"/>

# Configuration

<a name="config"/>

## Config files

Basic application configuration for running agent. Example configuration is stored in config directory.

### Files

* application.yaml - configuration for external integrations, such as agent exchange or data source; 
  and for Sentry - to monitor running on remote. Before remote launch, modify 'sentry' config section: 
  set 'turn_on' to _true_, and 'environment' and 'release' to appropriate values.


* reduction-rules.yaml - configuration for [scores reduction](#scores-reduction)

* name-preconditions.yaml - configuration of name preconditions necessary to get full solutions range for [scores reduction](#scores-reduction)

### Source

Default configuration directory is `./config/`. It can be changed by:
* environment variable `AGENT_CONFIGURATION_DIR`
* argument `-c` or `--configuration-dirs` while starting the agent 

Currently there is no way to use different configuration in one running agent.


<a name="implementation"/>

# Implementation

* Introduction
* Name Parsing
* Comparing Names
  * Comparison Algorithm Details
* Solution Reduction
  * Minimal Solution from Name Preconditions
  * Reduction Rules
  * Final Reduction


## Introduction

Given two lists of names the agent approaches resolution in a few steps. The agent is given a set of names that generated the alerts, the alerting party (```ap_names```), and a set of names on the watchlist that were matched against by at least one of the ```ap_names```, the watchlist party (```wl_names```) and returns a solution for each pair of names in the Cartesian product of ```ap_names``` and ```wl_names```. The solution can be one of ```MATCH```, ```NO_MATCH```, or ```INCONCLUSIVE```.

The following example will be used to illustrate the high-level process:

```
ap_names = {"Alphabet Inc.", "Bank Iowa"} 
wl_names= {"Bank of China", "Gazprom"}
```

**NOTE:** There are more attributes and output associated with each of these steps, however, for clarity, only the pertinent information is presented here. Comprehensive details and examples of output will be provided in the corresponding sections.


* The first step is parsing the names into ```NameInformation``` objects found in the organization-name-knowledge package.
	* Example ```Alphabet Inc.```:
	  * ```source="alphabet inc"```
	  * ```common_prefixes=""```
	  * ```base="alphabet"```
	  * ```common_suffixes=""```
	  * ```legal="inc"```
	  * ```countries=""```
	  * ```parenthesis=""```
	  * ```other=""```

* The second step is to produce the Cartesian product of name pairs using ```ap_names``` and ```wl_names```:
	* Example:
	  * ```("Alphabet Inc.", "Bank of China")```
	  * ```("Alphabet Inc.", "Gazprom") ```
	  * ```("Bank Iowa", "Bank of China") ```
	  * ```("Bank Iowa", "Gazprom") ```

* The third step is to compare each pair of names. This is accomplished by producing a feature vector comprised of outputs from various algorithms. 
	* Example ```("Bank Iowa", "Bank of China")```
	  * ```parenthesis_match = 0```
	  * ```abbreviation = 0 ```
	  * ```fuzzy_on_base = 0.6316```
	  * ```fuzzy_on_suffix = 0 ```
	  * ```fuzzy = 0.6316```
	  * ```partial_fuzzy = 0.7692```
	  * ```sorted_fuzzy = 0.6364```
	  * ```legal_terms = 0```
	  * ```tokenization = 0.3333```
	  * ```absolute_tokenization = 1```
	  * ```blacklisted = 0```
	  * ```country = 0```
	  * ```phonetics_on_base = 0.6667```
	  * ```phonetics = 0.6667```
	  * ```potential_subsidiary = 0```
	  * ```token_inclusion = 0```
	  * ```first_token = 1```

* The fourth step is to reduce each pair to a solution and the solution probability (agent's confidence in the solution).
	* Example:
	  * ```("Alphabet Inc.", "Bank of China") = (solution="NO_MATCH", solution_probability=0.9567)```
	  * ```("Alphabet Inc.", "Gazprom") = (solution="NO_MATCH", solution_probability=0.9878)```
	  * ```("Bank Iowa", "Bank of China") = (solution="MATCH", solution_probability=1)```
	  * ```("Bank Iowa", "Gazprom") = (solution="NO_MATCH", solution_probability=0.9858)```

* The final step is to reduce all solutions to a single solution. This is done by selecting the pair solution with the highest solution probability with preference given to pairs determined to be a match. A ```MATCH``` with any solution probability will be preferred to a ```NO_MATCH``` with high solution probability.
	* Example:
	  * Solution for
	    ```
	    ap_names = {"Alphabet Inc.", "Bank Iowa"} 
	    wl_names= {"Bank of China", "Gazprom"}
	    ```
	    is ```MATCH``` with ```solution_probability=1```

In the sections that follow, more detail will be given to each of the steps and algorithms employed by the agent to arrive at solutions.


<a name="name-parsing"/>

## Name Parsing

Name parsing is performed by the organization-name-knowledge package. This package utilizes rules-based parser to extract components from a given name and package them into a ```NameInformation``` object for use by the organization name agent. Detail on the parsing mechanism will not be given here, rather, only a brief description of what is in each component will be given. It is recommended to review the documentation in the organization-name-knowledge package.

```NameInformation``` has several attributes:

  * ```source``` contains the original name
  * ```common_prefixes``` contains words commonly seen at the beginning of names
  * ```base``` contains the words remaining after removing prefixes, legal terms, and suffixes
  * ```common_suffixes``` contains words commonly seen at the end of names that are not legal identifiers
  * ```legal``` contains common legal terms and abbreviations (e.g. 'corporation', 'ltd', etc.)
  * ```countries``` contains any country names found in the name
  * ```parenthesis``` contains words found in parenthesis
  * ```other``` contains other terms not found above

For each attribute, the original, unmodified string and the lower-cased string with some punctuation removed are stored. The cleaned string is often the one used in the compare step. For additional information regarding how this information is parsed, please see the organization-name-knowledge package.

<a name="comparing"/>

## Comparing Names

With the names parsed and the Cartesian product of combinations determined, each pair is now compared by the agent. This is done using a collection of algorithms that return numeric values. The output from all of these algorithms is referred to as the **feature vector** for the name pair. At this point, no decisions are made by the agent.

#### Examples of Output
The first example uses one of the pairs above. The second example uses two names that produce more results for all of the features to illustrate what each of the algorithms produce.

```python
from company_name.compare import compare


compare("Bank Iowa", "Bank of China")

parenthesis_match                Score(status='NO_DATA', value=0, compared=((), ()))
abbreviation                     Score(status='OK', value=0, compared=(('Bank Iowa',), ('Bank of China',)))
fuzzy_on_base                    Score(status='OK', value=0.6316, compared=(('Bank Iowa',), ('Bank of China',)))
fuzzy_on_suffix                  Score(status='NO_DATA', value=0.0, compared=((), ()))
fuzzy                            Score(status='OK', value=0.6316, compared=(('Bank Iowa',), ('Bank of China',)))
partial_fuzzy                    Score(status='OK', value=0.7692, compared=(('Bank Iowa',), ('Bank of China',)))
sorted_fuzzy                     Score(status='OK', value=0.6364, compared=(('Bank Iowa',), ('Bank of China',)))
legal_terms                      Score(status='NO_DATA', value=0, compared=((), ()))
tokenization                     Score(status='OK', value=0.3333, compared=(('Bank', 'Iowa'), ('Bank', 'of', 'China')))
absolute_tokenization            Score(status='OK', value=1, compared=(('Bank', 'Iowa'), ('Bank', 'of', 'China')))
blacklisted                      Score(status='OK', value=0.0, compared=((), ()))
country                          Score(status='NO_DATA', value=0.0, compared=((), ()))
phonetics_on_base                Score(status='OK', value=0.6667, compared=(('PNK (Bank Iowa)',), ('PNKFXN (Bank of China',)))
phonetics                        Score(status='OK', value=0.6667, compared=(('PNK (Bank Iowa)',), ('PNKFXN (Bank of China',)))
potential_subsidiary             Score(status='NO_DATA', value=0.0, compared=((), ()))
token_inclusion                  Score(status='NOT_APPLICABLE', value=0.0, compared=((), ()))
first_token                      Score(status='OK', value=1.0, compared=(('Bank',), ('Bank',)))


################################
################################


compare("SNM INC.", "THE SOME NAME MANUFACTURING (PRIVATE) LIMITED (FRANCE) FUNDED IN 1917 (SNM)")

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

### Comparison Algorithm Details
Most scores are computed on cleaned names - lowercase and without national characters.

* **parenthesis_match**
  ```
  "HP, INC" vs "SOME NAME (HP)"
  Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=((), ('HP',)))
  ```
  Possible Values: 0 or 1
  
  Compared: prefixes + base + suffixes with one of parenthesis value

  Algorithm: The algorithm checks if there are strings of characters contained in parenthesis in both names. It then checks if the cleaned name (without legal identifiers) appears in the string enclosed by parenthesis in the other. If either cleaned name is contained within the enclosed string then a 1 is returned. In the example above, the empty string is contained in 'HP'


* **abbreviation**
  ```
  "HP, INC" vs "HEWLETT-PACKARD COMPANY"
  Score(status=<ScoreStatus.OK: 'OK'>, value=1, compared=(('HP',), ('HEWLETT-PACKARD',)))
  ```
  Possible Values: between 0 and 1
  
  Compared: base (assumed abbreviation) with prefixes + base + optionally suffixes + optionally legal (assumed source of abbreviation)
  
  Algorithm: checking possible matches, traversing from left to right side of source and abbreviation simultaneously -
  acceptable non-obvious operations are:
    * using more letters from one word (but only consecutive ones)
      * Example: "picture" -> "pic"
    * skipping weak words
      * Example: "Imaginary Name of the Western Group" -> "INWG"
    * dropping suffixes and legals from the end, if they don't appear in abbreviation (but base words are obligatory)
      * Example: If abbreviation is "XYZ" then "Inc" is dropped from "Xxx Yyy Zzz Inc"
    * digit as possible multiplication of next letter
      * Example: "AI2" -> "AII"
    * treading hyphenated words as one or two
      * Example: "Hewlett-Packard" is treated as "Hewlett Packard" and ["Hewlett","Packard"]

  
* **fuzzy_on_base**, **fuzzy_on_suffix**, **fuzzy**
  ```
  fuzzy                Score(status='OK', value=0.79, compared=(('Indonesia North Industries',), ('North Indonesia Industries',)))
  fuzzy_on_base        Score(status='OK', value=0.64, compared=(('Indonesia North',), ('North Indonesia',)))
  fuzzy_on_suffix      Score(status='OK', value=1.0, compared=(('Industries',), ('Industries',)))
  ```
  Possible Value: between 0 and 1
  
  Compared: base / suffixes / prefixes + base + suffixes
  
  Algorithm: ratio from rapidfuzz library with removed spaces (using Levenshtein distance)
    * fuzzy: calculates the Levenshtein distance ratio between the two names without removing tokens
    * fuzzy_on_base: calculates the Levenshtein distance ratio between two names without legal identifiers
    * fuzzy_on_suffix: valculates the Levenshtein distance ratio between the suffixes of the two names

  
* **partial_fuzzy**, **sorted_fuzzy**
  ```
  fuzzy                Score(status='OK', value=0.79, compared=(('Indonesia North Industries',), ('North Indonesia Industries',)))
  partial_fuzzy        Score(status='OK', value=0.88, compared=(('Indonesia North Industries',), ('North Indonesia Industries',)))
  sorted_fuzzy         Score(status='OK', value=1.0, compared=(('Indonesia North Industries',), ('North Indonesia Industries',)))
  ```
  Possible Value: between 0 and 1
  
  Compared: prefixes + base + suffixes
  
  Algorithm: partial ratio / sorted ratio from rapidfuzz library with removed spaces (using Levenshtein distance)
    * partial_fuzzy: returns the maximum Levenshtein distance ratio found by computing the Levenshtein distance ratios for the shorter name against all substrings of equal length in the longer name
    * sorted_fuzzy: sorts the tokens alphabetically then calculates the Levenshtein distance ratio

  
* **legal_terms**
  ```
  Score(status='OK', value=0.75, compared=(('private', 'limited'), ('ltd',)))
  ```
  Possible Value: between 0 and 1
  
  Compared: legal
  
  Algorithm: match combined legal terms meaning
    (or, if meaning not available in datasource, exact legal terms after expanding the abbreviation) -
    order is not taken into consideration
  
  
* **absolute_tokenization**
  ```
  Score(status='OK', value=3, compared=(('THE', 'EMBASSY', 'OF', 'THE', 'REPUBLIC', 'OF', 'ANGOLA', 'IN', 'THE', 'REPUBLIC', 'OF', 'KENYA'), ('EMBASSY', 'OF', 'THE', 'REPUBLIC', 'OF', 'ANGOLA')))
  ```
  Possible Value: positive integers
  
  Compared: prefixes + base + suffixes
  
  Algorithm: number of tokens present in both names, without weak words

  
* **tokenization**
  ```
  Score(status='OK', value=0.5, compared=(('THE', 'EMBASSY', 'OF', 'THE', 'REPUBLIC', 'OF', 'ANGOLA', 'IN', 'THE', 'REPUBLIC', 'OF', 'KENYA'), ('EMBASSY', 'OF', 'THE', 'REPUBLIC', 'OF', 'ANGOLA')))
  ```
  Possible Value: between 0 and 1
  
  Compared: prefixes + base + suffixes
  
  Algorithm: ratio of tokens present in both names, without weak words

  
* **blacklisted**
  ```
  "Gazprom" vs "HP"
  Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=(('gazprom',), ()))
  ```
  
  Possible Value: 0 or 1
  
  Compared: cleaned input on alerted party side
  
  Algorithm: search for defined words (does not need exact word, for example "gazprom" works for "gazprombank")


* **country**
  ``` 
  "TOYOTA MOTOR FINANCE (CHINA)" vs "TOYOTA MOTOR FINANCE (THE PEOPLE'S REPUBLIC OF CHINA)"
  Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=(('CHINA',), ("THE PEOPLE'S REPUBLIC OF CHINA",)))
  ```
  
  Possible Value: between 0 and 1
  
  Compared: countries
  
  Algorithm: match countries without taking their order into consideration


* **phonetics_on_base**, **phonetics**
  ```
  "John Smith import & export" vs "Jon Smit imp & exp"
  phonetics_on_base:
    Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=(('ANSMT (John Smith)',), ('ANSMT (Jon Smit)',)))
  phonetics:
    Score(status=<ScoreStatus.OK: 'OK'>, value=0.83, compared=(('ANSMTMPRTKSPRT (John Smith import & export)',), ('ANSMTMPKSP (Jon Smit imp & exp)',)))
  ```
  Possible Value: between 0 and 1
  
  Compared: base / prefixes + base + suffixes, original (not cleaned)
  
  Algorithm: dmetaphone (from library phonetics, which do not support e.g. chinese or arabic) Double Metaphone is a phonetic algorithm that reduces a word to a combination of 12 consonant sounds. If there is ambiguity in the pronunciations, then two tokens are returned. Double Metaphone uses a set of rules to remove characters from a word before mapping the remaining characters to one of the 12 consonant sounds.
  

* **token_inclusion**
  ```
  "The Walt Disney Company" vs "Disney"
   Score(status=<ScoreStatus.OK: 'OK'>, value=1.0, compared=(('The', 'Walt', 'Disney'), ('Disney',)))
  ```
  Possible Value: 0 or 1
  
  Compared: prefixes + base + suffixes
  
  Algorithm: if exactly one word in exactly one of the names, check if this token also appears in the other name


* **first_token**
  ```
  "Allstate Identity Protection" vs "Allstate Heritage Life Insurance Company"
   Score(status='OK', value=1.0, compared=((Allstate',), ('Allstate',)))
  
  ```
  Possible Value: 0 or 1

  Compared: first token of base

  Algorithm: Check if the first token of both cleaned names is the same

<a name="solution-reduction"/>
## Solution Reduction
The feature vectors that were computed in the previous step are now used to produce a solution (```MATCH```/```NO_MATCH```/```INCONCLUSIVE```/etc.) for each of the name pairs. Solutions for each pair are produced using a set of rules defined in the ```reduction-rules.yaml``` config file.

### Minimal Solution from Name Preconditions
If either name in the pair does not meet the conditions defined in the ```name-preconditions.yaml``` config file, the pair will be solved as ```INCONCLUSIVE``` rather than ```NO_MATCH```.

### Reduction Rules
The rules are applied in the order they appear in the ```reduction-rules.yaml``` config file. There are two types of rules; a basic feature rule and a model rule.

* basic feature rule
    ```yaml
    - feature: blacklist
      threshold: 1
      solution: MATCH
    - feature: abbreviation
      threshold: 0.9
      solution: MATCH
      solution_probability: 0.8
    ```
   The rule allows the feature to be specified, the threshold required to apply the solution, and, if defined, return the probability for the specified solution. In the case where ```solution_probability``` is not defined, the probability is set to be the value for that feature. In the blacklist rule, the returned soltuion probability would be 1, for example.

* model rule
    ```yaml
      - source: model/some-sklearn-model.bin
        solutions:
          - solution: NO_MATCH
            label: NO_MATCH
            threshold: 0.85
          - solution: MATCH
            label: MATCH
            threshold: 0.15
    ```
    The rule uses the model found in the file specified in ```source```. The feature vector for the name pair is provided to the model and one of the specified solutions are applied. In the above rule, the first solution that is checked is the ```NO_MATCH```, the model probability for the ```NO_MATCH``` class is compared against the threshold, if the threshold is not surpassed then the next solution is applied. For this rule, the ```solution_probability``` is equal to the model probability for the class whose threshold is met. I.e. if the ```MATCH``` probability is 0.34, then ```solution_probability = 0.34``` and the solution is ```MATCH```.

The source is always relative to config directory and contains a model - currently only sklearn models are supported.
    The model can be binary or multi-class. ```solution_probability``` is taken from model probability.
    The model probability for ```MATCH``` or ```NO_MATCH``` must be met for that solution to be returned and the solutions are applied in the order they appear in the config file.

  The order that rules are written in the config file are the order the rules are applied to a name pair. For example, in the default config file in this repo the rules are, in order:
  
  1. If ```blacklisted == 1```, then ```MATCH``` with solution probability 1
  2. Else, if ```token_inclusion==1``` , then ```MATCH``` with solution probability 1
  3. Else, if ```partial_fuzzy==1``` , then ```MATCH``` with solution probability 1
  4. Else, if ```first_token==1``` , then ```MATCH``` with solution probability 1
  5. Else, if ```abbreviation>=0.9```, then ```MATCH``` with solution probability 0.8
  6. Else, if ```model(alerted_party_name, wl_name)[1] >= 0.85```, then ```NO_MATCH``` with solution probability equal to the ```NO_MATCH``` likelihood (i.e. ```model(alerted_party_name, wl_name)[1]```)
  7. Else, if ```model(alerted_party_name, wl_name)[0] >= 0.15```, then ```MATCH``` with solution probability equal to the ```MATCH``` likelihood
  8. Else, ```INCONCLUSIVE```
  
  **NOTE:** It is possible to set the thresholds of the model ```MATCH```/```NO_MATCH``` likelihoods so that ```INCONCLUSIVE``` is returned if neither condition is met. For example, if the threshold for ```NO_MATCH``` is set to 0.85 and the threshold for ```MATCH``` is set to 0.2, then if the model returns a likelihood of 0.83 for ```NO_MATCH``` and a likelihood of 0.17 for ```MATCH```, then neither condition is met and the result will be ```INCONCLUSIVE```.
  
  All solutions used must be defined in `company_name/solution/solution.py` - but not all defined there needs to be used.
  If rules will not be comprehensive, the default solution is INCONCLUSIVE.

Consider an example using one of the pairs from the running example. Values are taken from the output in the Comparison Algorithms example.
  * Example ```("Bank Iowa", "Bank of China")```:
    1. ```blacklisted = 0``` and the condition **is not** met, so the next rule is applied
    2. ```token_inclusion = 0``` and the condition **is not** met, so the next rule is applied
    3. ```partial_fuzzy = 0.7692``` and the condition **is not** met, so the next rule is applied
    4. ```first_token = 1``` and the condition **is** met, so the solution is ```MATCH``` with ```solution_probability = 1``` as defined in the rule

For the sake of completion, the model rule will be considered in isolation in the context of the above example. The model output for the example pair, ```("Bank Iowa", "Bank of China")``` is ```model_probs = [0.3002, 0.6998]```. The first value is the model probability that the pair is a match and the second is the probability that the pair is not a match. Applying the model rule above:
  1. ```model_probs[1] = 0.6998``` and the condition **is not** met, so the next part of the model rule is applied
  2. ```model_probs[0] = 0.3002``` and the condition **is** met, so the solution would be ```MATCH``` with ```solution_probability = 0.3002```

### Final Reduction
Once a solution and solution probability has been determined for each of the name pairs, they are grouped by solution and sorted by probability. In the running example, the ```MATCH``` pair is returned as it is the only name pair that was determined to be a ```MATCH``` based on the rules in the ```reduction-rules.yaml``` file.

  * Example:
    * ```("Bank Iowa", "Bank of China") = (solution="MATCH", solution_probability=1)```
    * ```("Alphabet Inc.", "Gazprom") = (solution="NO_MATCH", solution_probability=0.9878)```
    * ```("Bank Iowa", "Gazprom") = (solution="NO_MATCH", solution_probability=0.9858)```
    * ```("Alphabet Inc.", "Bank of China") = (solution="NO_MATCH", solution_probability=0.9567)```

And so the ultimate solution returned by an agent given ```ap_names``` and ```wl_names``` would be ```MATCH``` with probability 1 for ```("Bank Iowa", "Bank of China")```



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

For jenkins runs, flag `--without-rabbitmq` is added - so tests with agent exchange
are not run, as it needs running rabbitmq instance. 

To run locally tests that depends on rabbit, use in example this repo:
https://gitlab.silenteight.com/sens/common-docker-infrastructure
