from setuptools import find_packages, setup

install_require = [
    "agent-base==0.23.3",
    "agents-api~=0.27.0",
    "aiohttp==3.7.4.post0",
    "data-source-api~=0.18.0",
    "googleapis-common-protos==1.53.0",
    "organizationname-agent-api~=0.27.0",
    "organization-name-knowledge==0.9.0",
    "phonetics==1.0.5",
    "python-consul2~=0.1.5",
    "pyyaml==5.4.1",
    "rapidfuzz==1.7.1",
    "scikit-learn==1.0.1",
    "sentry_sdk==1.5.4",
    "unidecode==1.2.0",
]

tests_require = [
    "black~=22.3.0",
    "flake8-bugbear~=20.11.1",
    "flake8-comprehensions~=3.3.0",
    "flake8-import-order",
    "flake8-junit-report~=2.1.0",
    "flake8~=3.8.4",
    "isort",
    "pytest~=6.1.2",
    "shiv",
    "tox~=3.20.1",
]

setup(
    name="company-name",
    version="0.18.6",
    description="",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    packages=find_packages(exclude=("tests",)),
    classifiers=[
        "License :: Other/Proprietary License",
    ],
    python_requires=">=3.7.*",
    install_requires=install_require,
    extras_require={"tests": tests_require},
    setup_requires=[],
    tests_require=tests_require,
    entry_points={
        "console_scripts": [],
    },
)
