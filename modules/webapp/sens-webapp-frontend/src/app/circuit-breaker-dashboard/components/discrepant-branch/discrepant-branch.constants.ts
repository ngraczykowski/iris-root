import { StateContent } from '@app/ui-components/state/state';

export const prefix = 'circuitBreakerDashboard.discrepantBranches.';

export const stateLoading: StateContent = {
  title: prefix + 'state.loading.title',
  inProgress: true
};

export const stateError: StateContent = {
  title: prefix + 'state.error.title',
  description: prefix + 'state.error.description',
  button: prefix + 'state.error.button',
  centered: true,
};

export const translateKey = {
  headerId: prefix + 'header.id',
  headerDisabled: prefix + 'header.disabled',
  loading: prefix + 'state.loading',
  emptyState: prefix + 'state.empty',
  errorState: prefix + 'state.error',
};
