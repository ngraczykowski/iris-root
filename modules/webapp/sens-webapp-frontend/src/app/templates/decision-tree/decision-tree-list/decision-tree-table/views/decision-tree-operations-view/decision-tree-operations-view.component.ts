import { Component, Input, OnInit } from '@angular/core';
import { CellViewFactory } from '@app/components/dynamic-view-table/table-data-mapper';
import { View } from '@app/components/dynamic-view/dynamic-view.component';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Authority } from '@app/shared/security/principal.model';
import { DecisionTree, DecisionTreeStatus } from '@app/templates/model/decision-tree.model';
import { Observable } from 'rxjs';
import {
  DecisionTreeOperation,
  DecisionTreeOperationService
} from '../../../../decision-tree-operations/decision-tree-operation.service';

export interface DecisionTreeOperationsViewData {
  status: DecisionTreeStatus;
  decisionTreeId: number;
  decisionTreeName: string;
}

@Component({
  selector: 'app-decision-tree-operations-view',
  templateUrl: './decision-tree-operations-view.component.html',
  styleUrls: ['./decision-tree-operations-view.component.scss']
})
export class DecisionTreeOperationsViewComponent implements OnInit {

  @Input() data: DecisionTreeOperationsViewData;

  hasPermissionToCopyTree: Observable<boolean>;
  hasPermissionToRemoveTree: Observable<boolean>;

  constructor(
      private authenticatedUser: AuthenticatedUserFacade,
      private decisionTreeOperationService: DecisionTreeOperationService
  ) { }

  ngOnInit() {
    this.hasPermissionToCopyTree = this.authenticatedUser.hasAuthority(Authority.DECISION_TREE_MANAGE);
    this.hasPermissionToRemoveTree = this.authenticatedUser.hasSuperuserPermissions();
  }

  onRemove() {
    this.decisionTreeOperationService.openOperationWindow(DecisionTreeOperation.REMOVE, {
      id: this.data.decisionTreeId,
      name: this.data.decisionTreeName
    });
  }

  onCopy() {
    this.decisionTreeOperationService.openOperationWindow(DecisionTreeOperation.COPY, {
      id: this.data.decisionTreeId,
      name: this.data.decisionTreeName
    });
  }
}

export class DecisionTreeOperationsCellViewFactory implements CellViewFactory<DecisionTree> {
  create(entry: DecisionTree): View {
    return {
      component: DecisionTreeOperationsViewComponent,
      data: <DecisionTreeOperationsViewData> {
        status: entry.status,
        decisionTreeId: entry.id,
        decisionTreeName: entry.name
      }
    };
  }
}
