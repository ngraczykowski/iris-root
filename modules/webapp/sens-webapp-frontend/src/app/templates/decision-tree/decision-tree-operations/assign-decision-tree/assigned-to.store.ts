import { Injectable } from '@angular/core';
import { Set } from 'immutable';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { BatchType, BatchTypes, BatchTypeUpdates } from './assigned-to.model';
import { AssignedToService } from './assigned-to.service';

@Injectable()
export class AssignedToStore {

  private _assignedBatchTypes: BehaviorSubject<Set<BatchType>> = new BehaviorSubject(Set());
  public readonly assignedBatchTypes: Observable<BatchType[]> = this._assignedBatchTypes.asObservable()
      .map(set => set.toJS());
  private _availableBatchTypes: BehaviorSubject<Set<BatchType>> = new BehaviorSubject(Set());
  public readonly availableBatchTypes: Observable<BatchType[]> = this._availableBatchTypes.asObservable()
      .map(set => set.toJS());
  private _activatedBatchTypes: BehaviorSubject<Set<BatchType>> = new BehaviorSubject(Set());
  public readonly activatedBatchTypes: Observable<BatchType[]> = this._activatedBatchTypes.asObservable()
      .map(set => set.toJS());

  private decisionTreeId: number;
  private toActivate = Set().asMutable();
  private toDeactivate = Set().asMutable();
  private toAssign = Set().asMutable();
  private toUnassign = Set().asMutable();

  constructor(private assignedToService: AssignedToService) { }

  private static nameIsNotIn(batchType: BatchType, array: BatchType[]) {
    return array.map(value => value.name).indexOf(batchType.name) === -1;
  }

  fetchDecisionTreeBatchTypes(decisionTreeId: number): Observable<void> {
    this.decisionTreeId = decisionTreeId;
    this.clearUpdates();

    const getBatchTypesForTreeRequest = this.assignedToService.getBatchTypesForTree(decisionTreeId);
    const fetchCompleted = new Subject<void>();

    getBatchTypesForTreeRequest
        .pipe(
            finalize(() => fetchCompleted.complete()),
        ).subscribe(
        (batchTypes) => this.updateBatchTypesStoreState(batchTypes),
        (error) => fetchCompleted.error(error)
    );

    return fetchCompleted.asObservable();
  }

  saveBatchTypesAssignments(): Observable<void> {
    const saveCompleted = new Subject<void>();

    if (this.noChangesWereMade()) {
      saveCompleted.complete();
    } else {
      const request = this.assignedToService.updateBatchTypesForTree(this.decisionTreeId, this.createUpdates());
      request.subscribe(
          () => saveCompleted.complete(),
          (error) => saveCompleted.error(error)
      );
    }

    return saveCompleted.asObservable();
  }

  assign(toAssign: BatchType) {
    const hasNotBeenUnassigned = !this.toUnassign.has(toAssign);
    if (hasNotBeenUnassigned) {
      this.toAssign.add(toAssign);
    } else {
      this.toUnassign.remove(toAssign);
    }

    this._assignedBatchTypes.next(
        this._assignedBatchTypes.getValue()
            .add(toAssign));
    this._availableBatchTypes.next(
        this._availableBatchTypes.getValue()
            .remove(toAssign)
    );
  }

  activate(toActivate: BatchType) {
    const hasNotBeenDeactivated = !this.toDeactivate.has(toActivate);
    if (hasNotBeenDeactivated) {
      this.toActivate.add(toActivate);
    } else {
      this.toDeactivate.remove(toActivate);
    }

    this._activatedBatchTypes.next(
        this._activatedBatchTypes.getValue()
            .add(toActivate)
    );
    this._assignedBatchTypes.next(
        this._assignedBatchTypes.getValue()
            .remove(toActivate)
    );
  }

  unassign(toUnassign: BatchType) {
    const hasNotBeenAssigned = !this.toAssign.has(toUnassign);
    if (hasNotBeenAssigned) {
      this.toUnassign.add(toUnassign);
    } else {
      this.toAssign.remove(toUnassign);
    }

    this._availableBatchTypes.next(
        this._availableBatchTypes.getValue()
            .add(toUnassign)
    );
    this._assignedBatchTypes.next(
        this._assignedBatchTypes.getValue()
            .remove(toUnassign)
    );
  }

  deactivate(toDeactivate: BatchType) {
    const hasNotBeenActivated = !this.toActivate.has(toDeactivate);
    if (hasNotBeenActivated) {
      this.toDeactivate.add(toDeactivate);
    } else {
      this.toActivate.remove(toDeactivate);
    }

    this._assignedBatchTypes.next(
        this._assignedBatchTypes.getValue()
            .add(toDeactivate)
    );
    this._activatedBatchTypes.next(
        this._activatedBatchTypes.getValue()
            .remove(toDeactivate)
    );
  }

  private clearUpdates() {
    this.toActivate.clear();
    this.toDeactivate.clear();
    this.toAssign.clear();
    this.toUnassign.clear();
  }

  private updateBatchTypesStoreState(batchTypes: BatchTypes) {
    const activatedBatchTypes = batchTypes.activated;
    const assignedBatchTypes = batchTypes.assigned
        .filter(batchType => AssignedToStore.nameIsNotIn(batchType, activatedBatchTypes));
    const availableBatchTypes = batchTypes.available
        .filter(
            batchType => AssignedToStore.nameIsNotIn(batchType, assignedBatchTypes)
                && AssignedToStore.nameIsNotIn(batchType, activatedBatchTypes)
        );

    this._activatedBatchTypes.next(Set(activatedBatchTypes));
    this._assignedBatchTypes.next(Set(assignedBatchTypes));
    this._availableBatchTypes.next(Set(availableBatchTypes));
  }

  private noChangesWereMade() {
    return this.toActivate.isEmpty()
        && this.toDeactivate.isEmpty()
        && this.toAssign.isEmpty()
        && this.toUnassign.isEmpty();
  }

  private createUpdates(): BatchTypeUpdates {
    return {
      toDeactivate: this.toDeactivate.toJS(),
      toActivate: this.toActivate.toJS(),
      toAssign: this.toAssign.toJS(),
      toUnassign: this.toUnassign.toJS(),
    } as BatchTypeUpdates;
  }
}
