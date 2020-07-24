import { Injectable } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { StateWithAiSolutions } from '@core/ai-solutions/store/ai-solutions.state';
import { Solution } from '@endpoint/configuration/model/solution.enum';
import { select, Store } from '@ngrx/store';
import { AiSolutionsActions, AiSolutionsSelectors } from '@core/ai-solutions/store';
import { Observable } from 'rxjs';
import { shareReplay, switchMap, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AiSolutionsService {

  private availableSolutionsPipe: Observable<Solution[]> =
      this.authenticatedUserFacade.isLoggedIn().pipe(
          tap(() => this.store.dispatch(new AiSolutionsActions.LoadConfig())),
          switchMap(() => this.store.pipe(select(AiSolutionsSelectors.getAvailableSolutions))),
          shareReplay()
      );

  constructor(private store: Store<StateWithAiSolutions>,
              private authenticatedUserFacade: AuthenticatedUserFacade) { }

  public get availableSolutions(): Observable<Solution[]> {
    return this.availableSolutionsPipe;
  }

}
