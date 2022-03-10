from setuptools import find_packages, setup

tests_require = [
    "fuzzywuzzy==0.18.0",
    "omegaconf==2.1.1",
    "pyspark==3.1.1",
    "jupyter==1.0.0",
    "jupyterlab==3.3.0",
    "notebook==6.4.8",
    "spark-manager==0.9.2.dev0",
    "lxml==4.7.1",
]


setup(
    name="etl_pipeline",
    version="0.5.4-dev",
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
