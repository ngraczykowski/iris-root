from setuptools import setup, find_packages

install_require = [
    "agent-base>=0.4.0",
    "agents-api",
    "aiohttp",
    "data-source-api",
    "googleapis-common-protos",
    "grpcio",
    "grpcio-tools",
    "importlib_resources",
    "phonetics",
    "python-consul2",
    "pyyaml",
    "rapidfuzz",
    "scikit-learn",
    "unidecode",
]

tests_require = [
    "black>=20.8b1",
    "flake8-bugbear>=20.11.1",
    "flake8-comprehensions>=3.3.0",
    "flake8-import-order",
    "flake8-junit-report>=2.1.0",
    "flake8>=3.8.4",
    "isort",
    "mypy==0.790",
    "pytest-asyncio",
    "pytest>=6.1.2",
    "shiv",
    "tox>=3.20.1",
]

setup(
    name="company-name",
    version="0.10.0-dev",
    description="",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    packages=find_packages(exclude=("tests",)),
    classifiers=[
        "License :: Other/Proprietary License",
    ],
    python_requires="==3.7.*",
    install_requires=install_require,
    extras_require={"tests": tests_require},
    setup_requires=[],
    tests_require=tests_require,
    entry_points={
        "console_scripts": [],
    },
    package_data={
        "": ["resources/*"],
    },
)
