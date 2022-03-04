# importing the module
import argparse
import gitlab
import json
import os
import shutil
import subprocess
from pathlib import Path

GITLAB_URL = 'https://gitlab.silenteight.com/'
NOMAD_VAR_PATTERN = 's3::%s/artifacts/%s/%s'
MINIO_ADDRESS = 'https://minio.silenteight.com'
TMP_FOLDER = 'tmp'
CONF_FOLDER = 'conf'
ADDITIONAL_CONF_FOLDER = 'config'
LOCAL_ADDITIONAL_CONF_FOLDER = os.path.join(TMP_FOLDER, ADDITIONAL_CONF_FOLDER)
ENV_NAME = 'lima'
ARTIFACTS_FILE = 'artifacts_test.json'
NOMAD_FOLDER = 'nomad'
SUPPORTED_ENVS = ['dev', 'mike', 'lima']

parser = argparse.ArgumentParser()
parser.add_argument('private_token', help='Private token for %s authentication' % GITLAB_URL)
parser.add_argument('artifact_file', help='File with defined artifacts for deployment')
parser.add_argument('--dry_run', action='store_true', help='Dry run without nomad job start')
parser.add_argument('--env', help='Env on which deploy will be executed', required=False,
                    choices=SUPPORTED_ENVS)

os.environ['NOMAD_VAR_caddy_artifact'] = 'https://github.com/caddyserver/caddy/releases/download/' \
                                         'v2.4.2/caddy_2.4.2_linux_amd64.tar.gz'
os.environ['NOMAD_VAR_caddy_digest'] = 'sha512:9d3320f829cfd26945ed417bc4cb52f79691db6dce52ebc872' \
                                       '1d25c85a857888ae8db10ba500e1098aee92dc44a9d5fbe342b419d07' \
                                       '36f525712746f884cf1ff'
os.environ['NOMAD_ADDR'] = 'http://10.8.0.1:4646'


def _download_repository_files(artifact, repo_files, project):
    if len(repo_files) == 0:
        return

    for file in repo_files:
        path = os.path.join(TMP_FOLDER, file['path'])
        if file['type'] == 'tree':
            Path(path).mkdir(parents=True, exist_ok=True)
        else:
            with open(path, 'wb') as f:
                project.files.raw(file_path=file['path'], ref=artifact.branch, streamed=True, action=f.write)


def _set_up_minio_address(artifact):
    os.environ['NOMAD_VAR_%s_artifact_checksum' % artifact.nomad_name] = artifact.checksum
    os.environ['NOMAD_VAR_%s_artifact' % artifact.nomad_name] = NOMAD_VAR_PATTERN % (MINIO_ADDRESS, artifact.job_name, artifact.version)
    os.environ['NOMAD_VAR_%s_version' % artifact.nomad_name] = artifact.version


def _remove_config():
    try:
        shutil.rmtree(TMP_FOLDER)
    except:
        pass


class Deployer:
    gl = None
    dry_run = True
    env = ''

    def __init__(self, private_token, dry_run, env):
        self.gl = gitlab.Gitlab(url=GITLAB_URL, private_token=private_token)
        self.dry_run = dry_run
        self.env = env

    def deploy(self, artifacts):
        for artifact in artifacts:
            self._prepare_nomad_config(artifact)
            _set_up_minio_address(artifact)
            self._execute_nomad_deployment(artifact)
            if self.dry_run is False:
                _remove_config()

    def _prepare_nomad_config(self, artifact):
        print('Fetching config for job: %s' % artifact.job_name)
        project = self.gl.projects.get(artifact.project_id)

        if not os.path.exists(TMP_FOLDER):
            os.makedirs(TMP_FOLDER)

        if not os.path.exists(LOCAL_ADDITIONAL_CONF_FOLDER):
            os.makedirs(LOCAL_ADDITIONAL_CONF_FOLDER)

        nomad_files = project.repository_tree(path=NOMAD_FOLDER, ref=artifact.branch, recursive=True)

        additiona_configs = project.repository_tree(path=ADDITIONAL_CONF_FOLDER, ref=artifact.branch, recursive=True)

        _download_repository_files(artifact, nomad_files, project)
        _download_repository_files(artifact, additiona_configs, project)

    def _execute_nomad_deployment(self, artifact):
        cwd = os.path.join(os.getcwd(), 'tmp/nomad')
        nomad_file = '%s.nomad' % artifact.job_name
        execution_params = ['nomad', 'job', 'run']
        if self.env is None:
            var_file = '-var-file=%s.vars' % self.env
            execution_params.append(var_file)
        execution_params.append(nomad_file)

        if self.dry_run is True:
            print('Dry run for execution: %s on env: %s for nomad_name: %s' % (execution_params, self.env, artifact.nomad_name))
        else:
            print('Executing deployment for: \n%s \nto env: %s' % (artifact, self.env))
            subprocess.check_call(execution_params, env=dict(os.environ), cwd=cwd)


class ArtifactReader:
    artifact_file = ''

    def __init__(self, artifact_file):
        self.artifact_file = artifact_file

    def read_artifacts(self):
        with open(self.artifact_file) as json_file:
            data = json.load(json_file)
            artifacts = []
             
            for i in data:
                artifacts.append(ArtifactRepresentation(i))
                
        return artifacts


class ArtifactRepresentation:
    job_name = ''
    version = ''
    checksum = ''
    repo = ''
    project_id = ''
    branch = ''
    nomad_name = ''

    def __init__(self, json_dict):
        self.job_name = json_dict['jobName']
        self.version = json_dict['version']
        self.checksum = json_dict['checksum']
        self.repo = json_dict['repo']
        self.project_id = json_dict['projectId']
        self.branch = json_dict['branch']
        self.nomad_name = json_dict['nomadName']

    def __str__(self):
        return self.job_name + '\nversion: ' + self.version + '\nchecksum: ' + self.checksum + '\nrepo: ' + self.repo


if __name__ == '__main__':
    args = parser.parse_args()
    artifact_reader = ArtifactReader(args.artifact_file)
    deployer = Deployer(args.private_token, args.dry_run, args.env)
    deployer.deploy(artifact_reader.read_artifacts())
