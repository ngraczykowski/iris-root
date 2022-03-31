from setuptools import find_packages, setup

dev_require = [
    "fuzzywuzzy==0.18.0",
    "omegaconf==2.1.1",
    "grpcio==1.44.0",
    # "pyspark==3.1.1",
    "lxml==4.7.1",
    "googleapis-common-protos==1.55.0",
    "data-source-agentinput-api==0.21.0.5",
    "data-source-api==0.21.0.5",
    # "python-Levenshtein==0.12.2",
    # "spark-manager==0.10.0.dev0",
    "pandas>=1.1.5",
    "numpy>=1.19.5",
]


tests_require = [
    "fuzzywuzzy==0.18.0",
    "omegaconf==2.1.1",
    "spark-manager==0.9.2.dev0",
    "lxml==4.7.1",
    "data-source-agentinput-api==0.21.0.5",
    "data-source-api==0.21.0.5",
    "python-Levenshtein==0.12.2",
]


setup(
    name="etl_pipeline",
    version="0.5.14-dev",
    description="ETL pipeline",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    classifiers=[
        "License :: Other/Proprietary License",
    ],
    packages=find_packages(exclude=("tests",)),
    install_requires=dev_require,
    extras_require={
        "tests": tests_require,
    },
    setup_requires=[
        "pytest-runner>=5.1",
    ],
    tests_require=tests_require,
)
