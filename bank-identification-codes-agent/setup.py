from setuptools import setup, find_packages

tests_require = [
    "black>=19.10b0",
    "cattrs>=1.0.0",
    "flake8>=3.8.3",
    "flake8-bugbear>=20.1.4",
    "flake8-comprehensions>=3.2.3",
    "flake8-junit-report>=2.1.0",
    "pytest>=5.2.1",
    "pytest-cov>=2.10.0",
]
setup(
    name="ts-identification-mismatch-agent",
    version="1.1.0-dev",
    description="Transaction Screening Identification Mismatch Agent",
    author="Silent Eight Pte. Ltd.",
    author_email="proj-scb-tsaas@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    classifiers=["License :: Other/Proprietary License",],
    packages=find_packages(exclude=("tests",)),
    package_data={
        "idmismatchagent": ["templates/*",]
    },
    install_requires=[
        # Keep sorted alphabetically (case-insensitive)
        "ts-agent-toolkit>=2.1,==2.*",
    ],
    extras_require={
        "tests": tests_require,
    },
    setup_requires=["pytest-runner>=5.1",],
    tests_require=tests_require,
)
