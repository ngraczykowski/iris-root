import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { EmptyObservable } from 'rxjs/observable/EmptyObservable';
import { catchError, finalize } from 'rxjs/operators';
import { EventKey } from '../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../shared/event/local-event.service';
import { ErrorMapper } from '../../../../shared/http/error-mapper';
import { ServerError } from '../../../model/error.model';
import { BatchType } from './assigned-to.model';
import { AssignedToStore } from './assigned-to.store';

interface IDecisionTreeBatchType {
  batchType: string;
  decisionTreeName: string;
  decisionTreeId: number;
}

@Component({
  selector: 'app-assign-decision-tree',
  templateUrl: './assign-decision-tree.component.html',
  styleUrls: ['./assign-decision-tree.component.scss']
})
export class AssignDecisionTreeComponent implements OnInit {

  private static ERROR_MAPPER = new ErrorMapper({
    'ActivationAlreadyUsed': 'assignment.ACTIVATION_ALREADY_USED'
  }, 'decisionTree.message.batchTypesAssignments.error.');

  serverError?: ServerError;
  show: boolean;
  inProgress: boolean;
  isDirty = false;

  decisionTreeId: number;

  @Output() edit: EventEmitter<any> = new EventEmitter();

  constructor(private store: AssignedToStore, private eventService: LocalEventService) { }

  ngOnInit() { }

  close() {
    this.show = false;
  }

  open(decisionTree: { id: number }) {
    this.makePristine();
    this.startLoading();
    this.decisionTreeId = decisionTree.id;
    this.show = true;

    this.store.fetchDecisionTreeBatchTypes(this.decisionTreeId)
        .pipe(
            finalize(() => this.finishLoading()),
            catchError((error) => this.handleError(error))
        ).subscribe();
  }

  onSaveChanges() {
    this.startLoading();

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
        });
  }

  isBatchTypeAlreadyActivatedErrorServerError() {
    return this.serverError && this.serverError.error &&
        this.serverError.error.key === 'ActivationAlreadyUsed';
  }

  getErrorMessage() {
    return AssignDecisionTreeComponent.ERROR_MAPPER.get(this.serverError);
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

  onDeactivate(batchType: BatchType) {
    this.store.deactivate(batchType);
    this.makeDirty();
  }

  private finishLoading() {
    this.inProgress = false;
  }

  private makePristine() {
    this.clearError();
    this.isDirty = false;
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

  makeDirty() {
    this.isDirty = true;
  }

  private handleError(err): Observable<any> {
    this.serverError = err;
    return new EmptyObservable();
  }

  private clearError() {
    this.serverError = undefined;
  }
}
