import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import { DecisionTreeDetails } from '../../model/decision-tree.model';
import {
  DecisionTreeOperation,
  DecisionTreeOperationService,
  DecisionTreeOperationSuccessEvent
} from '../decision-tree-operations/decision-tree-operation.service';
import { DecisionTreeDetailsClient } from './decision-tree-details-client';

@Injectable()
export class DecisionTreeDetailsService {

  constructor(
      private client: DecisionTreeDetailsClient,
      private decisionTreeOperationService: DecisionTreeOperationService
  ) { }

  getDecisionTreeDetails(id): Observable<DecisionTreeDetails> {
    return this.client.getDecisionTreeDetails(id);
  }

  observeRefreshingOperationEvents(): Observable<DecisionTreeOperationSuccessEvent> {
    return this.decisionTreeOperationService
        .observeDecisionTreeOperationSuccessEvents(DecisionTreeOperation.EDIT, DecisionTreeOperation.ASSIGN);
  }

  observeGoBackOperationEvents(): Observable<DecisionTreeOperationSuccessEvent> {
    return this.decisionTreeOperationService
        .observeDecisionTreeOperationSuccessEvents(DecisionTreeOperation.REMOVE, DecisionTreeOperation.COPY);
  }
}
