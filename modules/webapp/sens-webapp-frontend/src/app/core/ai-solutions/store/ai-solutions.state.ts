import { LoaderState } from '@core/state/utils/loader-state';
import { Solution } from '@endpoint/configuration/model/solution.enum';

export const AI_SOLUTIONS_STATE_NAME = 'ai_solutions';

export interface AiSolutionsState extends LoaderState {
  availableSolutions: Solution[];
}

export interface StateWithAiSolutions {
  [AI_SOLUTIONS_STATE_NAME]: AiSolutionsState;
}

export const aiSolutionsInitialState: AiSolutionsState = {
  availableSolutions: []
};
