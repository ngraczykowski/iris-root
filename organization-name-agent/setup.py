from setuptools import setup, find_packages

tests_require = [
    "tox>=3.20.1",
    "black>=20.8b1",
    "mypy==0.790",
    "pytest>=6.1.2",
    "flake8>=3.8.4",
    "flake8-bugbear>=20.11.1",
    "flake8-comprehensions>=3.3.0",
    "flake8-junit-report>=2.1.0",
]

setup(
    name="company-name",
    version="0.1.0-dev",
    description="",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    packages=find_packages(exclude=("tests",)),
    classifiers=[
        "License :: Other/Proprietary License",
    ],
    python_requires=">=3.7",
    install_requires=[
        "phonetics",
        "fuzzywuzzy",
        "python-Levenshtein",
        "unidecode",
        "pyyaml"
    ],
    extras_require={
        "tests": tests_require,
    },
    setup_requires=[],
    tests_require=tests_require,
    entry_points={
        "console_scripts": [
        ],
    },
)
