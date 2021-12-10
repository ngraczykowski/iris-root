from setuptools import find_packages, setup

tests_require = [
    "black>=21.8b0",
    "cattrs==1.0.0",  # to be compatible with old attrs from ts-agent-toolkit
    "flake8>=3.8.3",
    "flake8-bugbear>=20.1.4",
    "flake8-comprehensions>=3.2.3",
    "flake8-junit-report>=2.1.0",
    "isort==5.9.3",
    "pytest>=5.2.1",
    "pytest-cov>=2.10.0",
    "pyspark==3.2.0",
    "serp-python-client>=1.1.0",
    "serp-protocol-agent>=0.24.0.118",
    "serp-protocol-base>=0.24.0.118",
    "serp-protocol-ns>=0.24.0.118",
    "agent-base==0.3.0",
    "agents-api==0.17.0.79",
    "aio-pika==6.8.0",
    "aiohttp==3.8.1",
    "aiormq==3.3.1",
    "aiosignal==1.2.0",
    "async-timeout==4.0.1",
    "attrs==19.3.0",
    "backports.entry-points-selectable==1.1.1",
    "certifi==2021.10.8",
    "charset-normalizer==2.0.7",
    "data-source-api==0.17.0.65",
    "delta==0.4.2",
    "distlib==0.3.3",
    "filelock==3.4.0",
    "frozenlist==1.2.0",
    "func-timeout==4.3.5",
    "googleapis-common-protos==1.53.0",
    "grpcio==1.42.0",
    "grpcio-health-checking==1.42.0",
    "grpcio-reflection==1.42.0",
    "grpcio-status==1.42.0",
    "grpcio-tools==1.42.0",
    "idna==3.3",
    "Jinja2==2.11.3",
    "lz4==3.1.3",
    "MarkupSafe==2.0.1",
    "multidict==5.2.0",
    "munch==2.5.0",
    "pamqp==2.3.0",
    "protobuf==3.19.1",
    "protocol-agents==0.17.0.79",
    "psutil==5.8.0",
    "py4j==0.10.9.2",
    "pyspark==3.2.0",
    "python-consul==1.1.0",
    "python-consul2==0.1.5",
    "PyYAML==5.4.1",
    "rapidfuzz==1.7.1",
    "requests==2.26.0",
    "serp-protocol-agent==0.35.0.169",
    "serp-protocol-base==0.37.0.175",
    "serp-protocol-ns==0.37.0.175",
    "serp-python-client==1.1.0",
    "six==1.16.0",
    "tox==3.24.4",
    "ts-agent-toolkit==2.1.0",
    "urllib3==1.26.7",
    "virtualenv==20.10.0",
    "yarl==1.7.2",
]


setup(
    name="etl_pipeline",
    version="0.5.0-dev",
    description="ETL pipeline",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    classifiers=[
        "License :: Other/Proprietary License",
    ],
    packages=find_packages(exclude=("tests",)),
    install_requires=tests_require,
    extras_require={
        "tests": tests_require,
    },
    setup_requires=[
        "pytest-runner>=5.1",
    ],
    tests_require=tests_require,
)
