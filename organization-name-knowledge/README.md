# Organization Name Knowledge

Package created to provide access to all organization - name - related knowledge, that was collected when developing
solution across various agents. Its purpose is to parse an organization name from a text that may be in cleaned, 
or completely freetext form.

## Installation:
### From source:
```pip install -e .```
### From PyPi:
```pip install organization-name-knowledge```

### Docker
#### Docker building 
Using agent inside Docker, its requirements are downloaded from our S8 PyPi. 
Thus, we need to do something like 'logging in' to be able to do so:
Create in project level directory, a file named "pip_index_url"
(this file is added to .gitignore and should never be copied anywhere !)
and place here your credentials to PYPI in form:

```https://user:password@repo.silenteight.com/artifactory/api/pypi/pypi/simple```

And then run build :

```DOCKER_BUILDKIT=1 docker build -t organization_name_knowledge --secret id=mysecret,src=../pip_index_url .```

#### Docker running
```docker run organization_name_knowledge ```

## Package API

### organization_name_knowledge.parse
Parse a name string to NameInformation object, that contains information about:
- source (text that was parsed)
- common_prefixes (found in name string)
- base (what is the base name of parsed organization name)
- common_suffixes (found in name string)
- legal (legal terms found withing name string, also from parentheses 
  (if parenthesis contains only legal terms, otherwise it is parsed as other name in parenthesis section)
- countries (country names found within name string - from parentheses only)
- parenthesis (information from parentheses found in string)
- other (tokens not match any category above, after legal terms)

i.e.
```python
import organization_name_knowledge
organization_name_knowledge.parse("The Silent Eight Pte Ltd of Singapore")
The Silent Eight PTE LTD Singapore (common_prefixes: The, base: Silent Eight, legal: Pte Ltd, countries: Singapore, other: of)
```
To get to know more about usage, check method docs

### organization_name_knowledge.parse_freetext
Parse a string to as many NameInformation objects, as number of organization names candidates found within the text.
It may be used to get organization names from longer freetext phrases, such as:

*This is the readme for this package - Silent Eight Pte Ltd product*

from which we want to get parsed as one of candidates a text *Silent Eight Pte Ltd*

To get to know more about usage, check method docs

## Resources

Static data used for comparison is currently stored in organization_name_knowledge/resources directory. It contains:
* common_prefixes.json - commonly used at the begging of organization name, such as "group"
* common_suffixes.json - commonly used at the end of organization name, such as "manufacturing", "sales" or "enterprise"
* conjunctions.txt - i.e. "and", "or", "for"
* countries.json - different names and abbreviations for countries
* joining_words.json - words such as "and"
* legal_terms.json - legal entity terms, with possible abbreviations and meaning
* markers.json - terms that indicates an organization name, but are not legal terms, such as 'bank'
* prepositions.txt - i.e. "from", "in", "of"
* sp_500.text - known huge organization names from SP 500 index
* weak_words.json - words ignored in creating abbreviation or extracting legal terms or common words
* words_mapping.json - mapping 
