import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from '@app/shared/auth/auth.service';
import { Authority } from '@app/shared/auth/principal.model';
import {
  DecisionTreeOperation,
  DecisionTreeOperationService
} from '@app/templates/decision-tree/decision-tree-operations/decision-tree-operation.service';
import { DecisionTreeDetails, DecisionTreePermission } from '@app/templates/model/decision-tree.model';

@Component({
  selector: 'app-decision-tree-management',
  templateUrl: './decision-tree-management.component.html',
  styleUrls: ['./decision-tree-management.component.scss']
})
export class DecisionTreeManagementComponent implements OnInit {

  @Input() decisionTree: DecisionTreeDetails;

  hasPermissionToRenameTree: boolean;
  hasPermissionToCopyTree: boolean;
  hasPermissionToRemoveTree: boolean;
  hasBatchTypeManageAccess: boolean;
  hasDecisionTreeViewPermission: boolean;

  constructor(
      private authService: AuthService,
      private decisionTreeOperationService: DecisionTreeOperationService
  ) { }

  ngOnInit() {
    this.hasPermissionToRenameTree = this.authService.hasAuthority(Authority.DECISION_TREE_MANAGE);
    this.hasPermissionToCopyTree = this.authService.hasAuthority(Authority.DECISION_TREE_MANAGE);
    this.hasPermissionToRemoveTree = this.authService.hasSuperuserPermissions();
    this.hasBatchTypeManageAccess = this.authService.hasAuthority(Authority.BATCH_TYPE_MANAGE);
    this.hasDecisionTreeViewPermission =
          this.decisionTree.permissions.indexOf(DecisionTreePermission.DECISION_TREE_VIEW) !== -1;
  }

  onRemoveDecisionTree() {
    this.decisionTreeOperationService.openOperationWindow(DecisionTreeOperation.REMOVE, this.decisionTree);
  }

  onRenameDecisionTree() {
    this.decisionTreeOperationService.openOperationWindow(DecisionTreeOperation.EDIT, this.decisionTree);
  }

  onAssignDecisionTree() {
    this.decisionTreeOperationService.openOperationWindow(DecisionTreeOperation.ASSIGN, this.decisionTree);
  }

  onCopyDecisionTree() {
    this.decisionTreeOperationService.openOperationWindow(DecisionTreeOperation.COPY, this.decisionTree);
  }
}
