import { Component, Input, OnInit } from '@angular/core';
import { CellViewFactory } from '@app/components/dynamic-view-table/table-data-mapper';
import { View } from '@app/components/dynamic-view/dynamic-view.component';
import { AuthService } from '@app/shared/auth/auth.service';
import { Authority } from '@app/shared/auth/principal.model';
import { DecisionTree, DecisionTreeStatus } from '@app/templates/model/decision-tree.model';
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

  hasPermissionToCopyTree: boolean;
  hasPermissionToRemoveTree: boolean;

  constructor(
      private authService: AuthService,
      private decisionTreeOperationService: DecisionTreeOperationService
  ) { }

  ngOnInit() {
    this.hasPermissionToCopyTree = this.authService.hasAuthority(Authority.DECISION_TREE_MANAGE);
    this.hasPermissionToRemoveTree = this.authService.hasSuperuserPermissions();
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
