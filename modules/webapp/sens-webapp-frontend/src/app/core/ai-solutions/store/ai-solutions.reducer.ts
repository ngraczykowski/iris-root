import {
  AiSolutionsAction,
  AiSolutionsActionTypes
} from '@core/ai-solutions/store/ai-solutions.actions';
import {
  aiSolutionsInitialState,
  AiSolutionsState
} from '@core/ai-solutions/store/ai-solutions.state';

export function aiSolutionsReducer(
  state = aiSolutionsInitialState,
  action: AiSolutionsAction
): AiSolutionsState {
  switch (action.type) {
    case AiSolutionsActionTypes.loadConfig:
      return {
        ...state,
        loading: true
      };
    case AiSolutionsActionTypes.loadConfigFail:
      return {
        ...state,
        loading: false,
        error: true
      };
    case AiSolutionsActionTypes.loadConfigSuccess:
      return {
        ...state,
        loading: false,
        availableSolutions: action.payload
      };
  }
}
