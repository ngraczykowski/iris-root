import { Component, EventEmitter, OnInit, Output, OnDestroy, ChangeDetectorRef, ViewContainerRef } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { ErrorMapper } from '@app/shared/http/error-mapper';
import { Observable, Subscription } from 'rxjs';
import { EmptyObservable } from 'rxjs/observable/EmptyObservable';
import { catchError, finalize } from 'rxjs/operators';
import { ServerError } from '../../../model/error.model';
import { BatchType } from './batch-type-management.model';
import { BatchTypeManagementStore } from './batch-type-management.store';
import { BatchTypeManagementService } from './batch-type-management.service';

interface IDecisionTreeBatchType {
  batchType: string;
  decisionTreeName: string;
  decisionTreeId: number;
}

@Component({
  selector: 'app-batch-type-management',
  templateUrl: './batch-type-management.component.html',
  styleUrls: ['./batch-type-management.component.scss']
})
export class BatchTypeManagementComponent implements OnInit, OnDestroy {
  private static ERROR_MAPPER = new ErrorMapper({
    'ActivationAlreadyUsed': 'assignment.ACTIVATION_ALREADY_USED'
  }, 'decisionTree.message.batchTypesAssignments.error.');
  selected;
  serverError?: ServerError;
  show: boolean;
  inProgress: boolean;
  isDirty = false;
  batchTypeNameFilter: string;
  decisionTreeId: number;
  toggleInUseActive = false;
  @Output() edit: EventEmitter<any> = new EventEmitter();

  private subscriptions: Subscription = new Subscription();

  constructor (
    private cdRef: ChangeDetectorRef,
    private batchManagementService: BatchTypeManagementService,
    private store: BatchTypeManagementStore,
    private eventService: LocalEventService
  ) { }

  ngOnInit() {
    this.subscriptions.add(
      this.batchManagementService.checkedElements.subscribe(data => {
        this.selected = data;
        this.cdRef.detectChanges();
      })
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  close() {
    this.show = false;
    this.batchManagementService.onDiscardChanges();
  }

  open(decisionTree: { id: number }) {
    this.makePristine();
    this.startLoading();
    this.decisionTreeId = decisionTree.id;
    this.show = true;

    this.subscriptions.add(this.store.fetchDecisionTreeBatchTypes(this.decisionTreeId)
        .pipe(
            finalize(() => this.finishLoading()),
            catchError((error) => this.handleError(error))
        ).subscribe());
  }

  onSaveChanges() {
    this.startLoading();

    this.subscriptions.add(
      this.store.saveBatchTypesAssignments()
        .pipe(
            finalize(() => this.finishLoading()),
            catchError((error) => this.handleError(error)),
        ).subscribe(
        {
          complete: () => {
            this.edit.emit();
            this.fireSaveSuccessEvent();
            this.close();
          }
        })
    );
  }

  isBatchTypeAlreadyActivatedErrorServerError() {
    return this.serverError && this.serverError.error &&
        this.serverError.error.key === 'ActivationAlreadyUsed';
  }

  getErrorMessage() {
    return BatchTypeManagementComponent.ERROR_MAPPER.get(this.serverError);
  }

  extractActivationErrorData(): IDecisionTreeBatchType[] {
    const data: IDecisionTreeBatchType[] = [];

    const extras = this.serverError.error.extras;
    for (const batchType of Object.keys(extras)) {
      data.push({
        batchType: batchType,
        decisionTreeId: extras[batchType].id,
        decisionTreeName: extras[batchType].name,
      });
    }

    return data;
  }

  getAvailableBatchTypes(): Observable<BatchType[]> {
    return this.store.availableBatchTypes;
  }

  getActivatedBatchTypes(): Observable<BatchType[]> {
    return this.store.activatedBatchTypes;
  }

  getAssignedBatchTypes(): Observable<BatchType[]> {
    return this.store.assignedBatchTypes;
  }

  onAssign(batchType: BatchType) {
    this.store.assign(batchType);
    this.makeDirty();
  }

  onUnassign(batchType: BatchType) {
    this.store.unassign(batchType);
    this.makeDirty();
  }

  onActivate(batchType: BatchType) {
    this.store.activate(batchType);
    this.makeDirty();
  }

  makeDirty() {
    this.isDirty = true;
  }

  showSelectedElementsPanel() {
    if (this.selected.length > 0) {
      return true;
    }
  }

  resetFilters() {
    this.batchTypeNameFilter = '';
    this.batchManagementService.resetUsedBatches();
    this.toggleInUseActive = false;
  }

  isFilteringActive() {
    if (this.batchTypeNameFilter || this.toggleInUseActive) {
      return true;
    }

    return false;
  }

  enableSaveButton() {
    return this.isFilteringActive() || !this.isDirty;
  }

  toggleInUse() {
    this.batchManagementService.toggleUsedBatches();
    this.toggleInUseActive = !this.toggleInUseActive;
    return this.toggleInUseActive;
  }

  moveToAvailable() {
    this.batchManagementService.checkedElements.value.forEach((batchType: BatchType) => {
      if (this.store.isActive(batchType)) {
        this.store.deactivate(batchType, 'available');
      } else if (this.store.isAssigned(batchType)) {
        this.store.unassign(batchType);
      }
    });
    this.batchManagementService.uncheckAll();
    this.makeDirty();
  }

  moveToAssigned() {
    this.batchManagementService.checkedElements.value.forEach((batchType: BatchType) => {
      if (this.store.isActive(batchType)) {
        this.store.deactivate(batchType, 'assign');
      } else if (this.store.isAvaiable) {
        this.store.assign(batchType);
      }
    });
    this.batchManagementService.uncheckAll();
    this.makeDirty();
  }

  moveToActive() {
    this.batchManagementService.checkedElements.value.forEach((batchType: BatchType) => this.store.activate(batchType));
    this.batchManagementService.uncheckAll();
    this.makeDirty();
  }

  uncheckNotMatchingBatches() {
    if (this.batchTypeNameFilter.length) {
      this.batchManagementService.uncheckNotMatchingFilterQuery(this.batchTypeNameFilter);
    }
  }

  private handleError(err): Observable<any> {
    this.serverError = err;
    return new EmptyObservable();
  }

  private clearError() {
    this.serverError = undefined;
  }

  private startLoading() {
    this.inProgress = true;
  }

  private fireSaveSuccessEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'decisionTree.message.edit.success'
      }
    });
  }

  private finishLoading() {
    this.inProgress = false;
  }

  private makePristine() {
    this.clearError();
    this.isDirty = false;
  }
}
