from setuptools import find_packages, setup

tests_require = [
    "black>=22.1.0",
    "flake8>=3.8.3",
    "flake8-bugbear>=20.1.4",
    "flake8-comprehensions>=3.2.3",
    "flake8-junit-report>=2.1.0",
    "isort==5.9.3",
    "pytest>=5.2.1",
    "pytest-cov>=2.10.0",
]
setup(
    name="organization-name-knowledge",
    version="0.10.0-dev",
    description="Organization Name Knowledge Package",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    classifiers=[
        "License :: Other/Proprietary License",
    ],
    packages=find_packages(exclude=("tests",)),
    package_data={"organization_name_knowledge": ["resources/*"]},
    install_requires=[
        # Keep sorted alphabetically (case-insensitive)
        "fastapi==0.70.0",
        "importlib-resources==5.2.2",
        "requests==2.26.0",
        "unidecode==1.2.0",
        "uvicorn==0.15.0",
    ],
    extras_require={
        "tests": tests_require,
    },
    setup_requires=[
        "pytest-runner>=5.1",
    ],
    tests_require=tests_require,
)
