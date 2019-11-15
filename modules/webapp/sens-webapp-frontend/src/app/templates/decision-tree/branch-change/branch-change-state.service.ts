import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import {
  BranchChangeState,
  BranchChangeStateType,
  BranchSelectStateData,
  BranchUpdateStateData
} from './branch-change.component';

@Injectable()
export class BranchChangeStateService {

  private subject: Subject<BranchChangeState> = new Subject();

  observeChanges(): Observable<BranchChangeState> {
    return this.subject.asObservable();
  }

  setSelectState(data: BranchSelectStateData) {
    this.changeState({
      type: BranchChangeStateType.SELECT,
      data: data
    });
  }

  setUpdateState(data: BranchUpdateStateData) {
    this.changeState({
      type: BranchChangeStateType.UPDATE,
      data: data
    });
  }

  setResetState(data: BranchUpdateStateData) {
    this.changeState({
      type: BranchChangeStateType.RESET,
      data: data
    });
  }

  private changeState(state: BranchChangeState) {
    this.subject.next(state);
  }
}
