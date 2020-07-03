import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  AbstractControl,
  AsyncValidatorFn,
  ValidationErrors
} from '@angular/forms';
import { DecisionTreesService } from '@core/decision-trees/services/decision-trees.service';
import { Observable, of, timer } from 'rxjs';
import { catchError, first, mapTo, switchMap } from 'rxjs/operators';

export const DECISION_TREE_EXISTS_VALIDATOR_ERROR: string = 'DECISION_TREE_EXISTS_VALIDATOR_ERROR';

@Injectable({
  providedIn: 'root'
})
export class DecisionTreeExistsValidator {

  constructor(public decisionTreesService: DecisionTreesService) { }

  public createValidator(debounce: number = 0): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return timer(debounce).pipe(
          switchMap(() => this.decisionTreesService.getDecisionTree(control.value)),
          mapTo(null),
          catchError((error: HttpErrorResponse) => {
            // @TODO: implement ErrorMapper.hasCode once it appears
            return of({[DECISION_TREE_EXISTS_VALIDATOR_ERROR]: true});
          }),
          first()
      );
    };
  }
}
