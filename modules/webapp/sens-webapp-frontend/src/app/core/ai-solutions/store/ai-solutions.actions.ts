import { Solution } from '@endpoint/configuration/model/solution.enum';
import { Action } from '@ngrx/store';

export enum AiSolutionsActionTypes {
  loadConfig = '[aiSolutions] load config',
  loadConfigFail = '[aiSolutions] load config fail',
  loadConfigSuccess = '[aiSolutions] load config success'
}

export class LoadConfig implements Action {
  readonly type = AiSolutionsActionTypes.loadConfig;
}

export class LoadConfigFail implements Action {
  readonly type = AiSolutionsActionTypes.loadConfigFail;
}

export class LoadConfigSuccess implements Action {
  readonly type = AiSolutionsActionTypes.loadConfigSuccess;
  constructor(public payload: Solution[]) {}
}

export type AiSolutionsAction =
    | LoadConfig
    | LoadConfigFail
    | LoadConfigSuccess;
