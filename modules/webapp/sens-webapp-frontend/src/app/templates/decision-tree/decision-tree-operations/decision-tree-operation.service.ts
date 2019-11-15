import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { filter } from 'rxjs/operators';

export enum DecisionTreeOperation {
  CREATE = 'CREATE', EDIT = 'EDIT', COPY = 'COPY', REMOVE = 'REMOVE', ASSIGN = 'ASSIGN'
}

export interface OpenDecisionTreeOperationWindowEvent {
  operation: DecisionTreeOperation;
  decisionTree?: { id, name };
}

export interface DecisionTreeOperationSuccessEvent {
  operation: DecisionTreeOperation;
}

@Injectable()
export class DecisionTreeOperationService {

  private openWindowSubject: Subject<OpenDecisionTreeOperationWindowEvent> = new Subject<OpenDecisionTreeOperationWindowEvent>();
  private operationSuccessSubject: Subject<DecisionTreeOperationSuccessEvent> = new Subject<DecisionTreeOperationSuccessEvent>();

  constructor() { }

  private static isOperationSupported(e: OpenDecisionTreeOperationWindowEvent, operations: DecisionTreeOperation[]) {
    return !operations || operations.length === 0 || operations.indexOf(e.operation) !== -1;
  }

  openOperationWindow(operation: DecisionTreeOperation, decisionTree?: { id, name }) {
    this.openWindowSubject.next(<OpenDecisionTreeOperationWindowEvent> {
      operation: operation,
      decisionTree: decisionTree
    });
  }

  operationSuccess(operation: DecisionTreeOperation) {
    this.operationSuccessSubject.next({operation});
  }

  observeOpenDecisionTreeOperationWindowEvents(...operations: DecisionTreeOperation[]): Observable<OpenDecisionTreeOperationWindowEvent> {
    return this.openWindowSubject.asObservable()
        .pipe(filter(e => DecisionTreeOperationService.isOperationSupported(e, operations)));
  }

  observeDecisionTreeOperationSuccessEvents(...operations: DecisionTreeOperation[]): Observable<DecisionTreeOperationSuccessEvent> {
    return this.operationSuccessSubject.asObservable()
        .pipe(filter(e => DecisionTreeOperationService.isOperationSupported(e, operations)));
  }
}
