import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Authority } from '@app/shared/security/principal.model';
import { Observable, Subscription } from 'rxjs';
import { BatchTypeManagementComponent } from './batch-type-management/batch-type-management.component';
import { CopyDecisionTreeComponent } from './copy-decision-tree/copy-decision-tree.component';
import { CreateNewDecisionTreeComponent } from './create-new-decision-tree/create-new-decision-tree.component';
import {
  DecisionTreeOperation,
  DecisionTreeOperationService,
  OpenDecisionTreeOperationWindowEvent
} from './decision-tree-operation.service';
import { EditDecisionTreeComponent } from './edit-decision-tree/edit-decision-tree.component';
import { RemoveDecisionTreeComponent } from './remove-decision-tree/remove-decision-tree.component';

@Component({
  selector: 'app-decision-tree-operations',
  templateUrl: './decision-tree-operations.component.html',
  styleUrls: ['./decision-tree-operations.component.scss']
})
export class DecisionTreeOperationsComponent implements OnInit, OnDestroy {

  hasDecisionTreeManageAccess: Observable<boolean>;
  hasBatchTypeManageAccess: Observable<boolean>;

  @ViewChild(RemoveDecisionTreeComponent, {static: false}) removeComponent: RemoveDecisionTreeComponent;
  @ViewChild(CopyDecisionTreeComponent, {static: false}) copyComponent: CopyDecisionTreeComponent;
  @ViewChild(EditDecisionTreeComponent, {static: false}) editComponent: EditDecisionTreeComponent;
  @ViewChild(CreateNewDecisionTreeComponent, {static: false}) createComponent: CreateNewDecisionTreeComponent;
  @ViewChild(BatchTypeManagementComponent, {static: false}) assignComponent: BatchTypeManagementComponent;

  private handlers: Map<DecisionTreeOperation, (e: OpenDecisionTreeOperationWindowEvent) => void> = new Map();

  private subscription: Subscription;

  constructor(private authenticatedUser: AuthenticatedUserFacade, private service: DecisionTreeOperationService) { }

  ngOnInit() {
    this.initAccess();
    this.initHandlers();
    this.startObserveOpenEvents();
  }

  private initAccess() {
    this.hasDecisionTreeManageAccess = this.authenticatedUser.hasAuthority(Authority.DECISION_TREE_MANAGE);
    this.hasBatchTypeManageAccess = this.authenticatedUser.hasAuthority(Authority.BATCH_TYPE_MANAGE);
  }

  ngOnDestroy() {
    this.stopObserveOpenEvents();
  }

  onRemoved() {
    this.service.operationSuccess(DecisionTreeOperation.REMOVE);
  }

  onCopied() {
    this.service.operationSuccess(DecisionTreeOperation.COPY);
  }

  onEdited() {
    this.service.operationSuccess(DecisionTreeOperation.EDIT);
  }

  onAssigned() {
    this.service.operationSuccess(DecisionTreeOperation.ASSIGN);
  }

  onCreated() {
    this.service.operationSuccess(DecisionTreeOperation.CREATE);
  }

  private initHandlers() {
    if (this.hasDecisionTreeManageAccess) {
      this.initDecisionTreeManageHandlers();
    }
    if (this.hasBatchTypeManageAccess) {
      this.initBatchTypeManageHandlers();
    }
  }

  private initDecisionTreeManageHandlers() {
    this.handlers.set(DecisionTreeOperation.CREATE, () => this.createComponent.open());
    this.handlers.set(DecisionTreeOperation.EDIT, e => this.editComponent.open(e.decisionTree));
    this.handlers.set(DecisionTreeOperation.COPY, e => this.copyComponent.open(e.decisionTree));
    this.handlers.set(DecisionTreeOperation.REMOVE, e => this.removeComponent.open(e.decisionTree));
  }

  private initBatchTypeManageHandlers() {
    this.handlers.set(DecisionTreeOperation.ASSIGN, e => this.assignComponent.open(e.decisionTree));
  }

  private handleEvent(event: OpenDecisionTreeOperationWindowEvent) {
    const handler = this.handlers.get(event.operation);
    if (handler) {
      handler(event);
    }
  }

  private startObserveOpenEvents() {
    this.subscription = this.service.observeOpenDecisionTreeOperationWindowEvents()
        .subscribe(e => this.handleEvent(e));
  }

  private stopObserveOpenEvents() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
