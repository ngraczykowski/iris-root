import os
import re
import sys

from setuptools import find_packages, setup

dev_require = [
    "agent-base",
    "data-source-agentinput-api==0.21.0.5",
    "data-source-api==0.23.0.12",
    "data-source-categories-api==0.23.0.11",
    "fuzzywuzzy==0.18.0",
    "googleapis-common-protos==1.55.0",
    "grpcio==1.44.0",
    "lxml==4.7.1",
    "omegaconf==2.1.1",
    "protobuf==3.19.4",
    "python-consul2==0.1.5",
    "pika==1.2.1",
]

tests_require = ["pandas==1.1.5"]

try:
    version = os.environ["PYTHON_PACKAGE_VERSION"]
except KeyError:
    version = sys.argv[1]
    if not re.match(r"^\d+\.\d+\.*\d*", version):
        raise ValueError(
            f"Provide version following SemVer scheme as a first argument. Given: {version}"
        )
    del sys.argv[1]
except IndexError:
    raise ValueError("Provide version as a first argument in setup.py run")


setup(
    name="etl_pipeline",
    version=version.replace("-", "+", 1),
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
    python_requires=">=3.7",
)
