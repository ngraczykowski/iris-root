from setuptools import setup, find_packages, find_namespace_packages
import os

setup(
    name=os.environ['PYTHON_PACKAGE_NAME'],
    description=os.environ['PYTHON_PACKAGE_DESCRIPTION'],
    version=os.environ['PYTHON_PACKAGE_VERSION'],
    author='Silent Eight Pte. Ltd.',
    author_email='support@silenteight.com',
    url='https://silenteight.com',
    license='Proprietary',
    packages=find_namespace_packages(include=['silenteight.*']),
    classifiers=[
        'License :: Other/Proprietary License',
    ]
)
