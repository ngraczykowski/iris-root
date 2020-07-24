import { Injectable } from '@angular/core';
import {
  AiSolutionsActionTypes,
  LoadConfigFail,
  LoadConfigSuccess
} from '@core/ai-solutions/store/ai-solutions.actions';
import { Solution } from '@endpoint/configuration/model/solution.enum';
import { ConfigurationEndpointService } from '@endpoint/configuration/services/configuration-endpoint.service';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { Observable, of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';

@Injectable()
export class AiSolutionsEffects {

  constructor(private actions: Actions,
              private configurationEndpointService: ConfigurationEndpointService) {}

  @Effect()
  loadConfig: Observable<LoadConfigFail | LoadConfigSuccess> =
      this.actions.pipe(
          ofType(AiSolutionsActionTypes.loadConfig),
          mergeMap(() => this.configurationEndpointService.solutions()
              .pipe(
                map((payload: Solution[]) => new LoadConfigSuccess(payload)),
                catchError(() => of(new LoadConfigFail()))
              )
          )
      );

}
