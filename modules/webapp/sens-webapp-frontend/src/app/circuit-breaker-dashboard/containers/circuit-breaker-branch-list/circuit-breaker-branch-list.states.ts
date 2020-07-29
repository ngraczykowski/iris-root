import { StateContent } from '@app/ui-components/state/state';

export const prefix = 'circuitBreakerDashboard.discrepantBranches.';

export const stateLoading: StateContent = {
  title: prefix + 'state.loading.title',
  inProgress: true,
  centered: true
};

export const stateEmptyList: StateContent = {
  title: prefix + 'state.empty.title',
  centered: true
};

export const stateError: StateContent = {
  title: prefix + 'state.error.title',
  description: prefix + 'state.error.description',
  button: prefix + 'state.error.button',
  centered: true
};
