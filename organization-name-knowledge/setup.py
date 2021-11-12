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
]
setup(
    name="organization-name-knowledge",
    version="0.4.0-dev",
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
        "unidecode==1.2.0",
    ],
    extras_require={
        "tests": tests_require,
    },
    setup_requires=[
        "pytest-runner>=5.1",
    ],
    tests_require=tests_require,
)
