import { Component, Input, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Authority } from '@app/shared/security/principal.model';
import {
  DecisionTreeOperation,
  DecisionTreeOperationService
} from '@app/templates/decision-tree/decision-tree-operations/decision-tree-operation.service';
import {
  DecisionTreeDetails,
  DecisionTreePermission
} from '@app/templates/model/decision-tree.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-decision-tree-management',
  templateUrl: './decision-tree-management.component.html',
  styleUrls: ['./decision-tree-management.component.scss']
})
export class DecisionTreeManagementComponent implements OnInit {

  @Input() decisionTree: DecisionTreeDetails;

  hasPermissionToRenameTree: Observable<boolean>;
  hasPermissionToCopyTree: Observable<boolean>;
  hasPermissionToRemoveTree: Observable<boolean>;
  hasBatchTypeManageAccess: Observable<boolean>;
  hasDecisionTreeViewPermission: boolean;

  constructor(
      private authenticatedUser: AuthenticatedUserFacade,
      private decisionTreeOperationService: DecisionTreeOperationService
  ) { }

  ngOnInit() {
    this.hasPermissionToRenameTree = this.authenticatedUser.hasAuthority(Authority.DECISION_TREE_MANAGE);
    this.hasPermissionToCopyTree = this.authenticatedUser.hasAuthority(Authority.DECISION_TREE_MANAGE);
    this.hasPermissionToRemoveTree = this.authenticatedUser.hasSuperuserPermissions();
    this.hasBatchTypeManageAccess = this.authenticatedUser.hasAuthority(Authority.BATCH_TYPE_MANAGE);
    // TODO(mmastylo): execute separate call for permissions
    this.hasDecisionTreeViewPermission = true;
    // this.hasDecisionTreeViewPermission =
    //     this.decisionTree.permissions.indexOf(DecisionTreePermission.DECISION_TREE_VIEW) !== -1;
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
