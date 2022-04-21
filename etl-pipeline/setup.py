from setuptools import find_packages, setup

dev_require = [
    "fuzzywuzzy==0.18.0",
    "omegaconf==2.1.1",
    "grpcio==1.44.0",
    "lxml==4.7.1",
    "googleapis-common-protos==1.55.0",
    "data-source-agentinput-api==0.21.0.5",
    "data-source-api>=0.23.0.12",
    "python-consul2==0.1.5",
    "data-source-categories-api==0.23.0.11",
]

tests_require = ["pandas==1.1.5"]

setup(
    name="etl_pipeline",
    version="0.5.23-dev",
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
        "tests": dev_require + tests_require,
    },
    setup_requires=[
        "pytest-runner>=5.1",
    ],
    tests_require=dev_require,
    python_requires=">=3.6",
)
