import { Injectable } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/auth/authenticated-user-facade.service';
import { Authority } from '@app/shared/auth/principal.model';
import { CollectionResponse } from '@app/templates/model/collection-response.model';
import { DecisionTree } from '@app/templates/model/decision-tree.model';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import {
  DecisionTreeOperation,
  DecisionTreeOperationService
} from '../decision-tree-operations/decision-tree-operation.service';
import { DecisionTreeListClient } from './decision-tree-list-client';

@Injectable()
export class DecisionTreeListService {

  constructor(
      private client: DecisionTreeListClient,
      private authenticatedUser: AuthenticatedUserFacade,
      private decisionTreeOperationService: DecisionTreeOperationService
  ) { }

  getActiveDecisionTrees(): Observable<CollectionResponse<DecisionTree>> {
    return this.client.getDecisionTrees({active: true});
  }

  getInactiveDecisionTrees(): Observable<CollectionResponse<DecisionTree>> {
    return this.client.getDecisionTrees({active: false});
  }

  hasDecisionTreeManageAccess(): Observable<boolean> {
    return this.authenticatedUser.hasAuthority(Authority.DECISION_TREE_MANAGE);
  }

  openCreateDecisionTreeWindow() {
    this.decisionTreeOperationService.openOperationWindow(DecisionTreeOperation.CREATE);
  }
}
