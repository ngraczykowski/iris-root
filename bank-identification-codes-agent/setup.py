from setuptools import setup, find_packages

setup(
    name="ts-identification-mismatch-agent",
    version="1.0.0-dev",
    description="Transaction Screening Identification Mismatch Agent",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    packages=find_packages(exclude=("tests",)),
    classifiers=["License :: Other/Proprietary License",],
    install_requires=[
        # Keep sorted alphabetically (case-insensitive)
    ],
    setup_requires=["pytest-runner>=5.1",],
    tests_require=[
        "black>=19.10b0",
        "flake8>=3.8.3",
        "flake8-bugbear>=20.1.4",
        "flake8-comprehensions>=3.2.3",
        "flake8-junit-report>=2.1.0",
        "pytest>=5.2.1",
        "pytest-cov>=2.10.0",
    ],
)
