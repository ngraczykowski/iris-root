from setuptools import find_packages, setup

tests_require = [
    "black~=22.3.0",
    "flake8-bugbear~=20.11.1",
    "flake8-comprehensions~=3.3.0",
    "flake8-import-order",
    "flake8-junit-report~=2.1.0",
    "flake8~=3.8.4",
    "isort==5.9.3",
    "pytest~=6.1.2",
    ]
setup(
    name="bank-identification-codes",
    version="2.5.3",
    description="Transaction Screening Bank Identification Codes Agent",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    packages=find_packages(exclude=("tests",)),
    classifiers=[
        "License :: Other/Proprietary License",
    ],
    python_requires=">=3.7.*",
    install_requires=[
        # Keep sorted alphabetically (case-insensitive)
        "agent-base==0.23.0",
        "bankidentificationcodes-agent-api~=0.27.0",
    ],
    extras_require={"tests": tests_require},
    setup_requires=[],
    tests_require=tests_require,
    entry_points={
        "console_scripts": [],
    },
)
