export interface ApplicationInfo {
  component: string;
  git: {
    commit: {
      time: string;
      id: string;
    },
    branch: string;
  };
  build: {
    version: string;
    artifact: string;
    name: string;
    group: string;
    time: string
  };
}
