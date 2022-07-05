## Agent Dictionaries

This library provides an abstraction for reading and loading all kind of resources that you need for
developing agent purposes.

### Dictionary Source

DictionarySource is an interface that allows creating dictionary instances one per source identifier
and dictionary class.

Agent Dictionaries library can read text lines from:

- Collection of Strings
- Configuration Files resolved by Agent Config Loader
- External Files
- Resources from the classpath

Agent Dictionaries library also allows you to create compound sources that consist of other sources.

### Dictionary

Dictionary is a marker interface for dictionary classes.

Agent Dictionaries library supports many commonly used formats and allows you to create your own
Dictionary classes.
