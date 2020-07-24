import {
  AI_SOLUTIONS_STATE_NAME,
  AiSolutionsState,
  StateWithAiSolutions
} from '@core/ai-solutions/store/ai-solutions.state';
import { Solution } from '@endpoint/configuration/model/solution.enum';
import { createFeatureSelector, createSelector, MemoizedSelector } from '@ngrx/store';

export const getAiSolutionsState: MemoizedSelector<StateWithAiSolutions, AiSolutionsState> =
    createFeatureSelector<AiSolutionsState>(AI_SOLUTIONS_STATE_NAME);

export const getAvailableSolutions: MemoizedSelector<StateWithAiSolutions, Solution[]> =
    createSelector(getAiSolutionsState,
        (state: AiSolutionsState) => state.availableSolutions);
