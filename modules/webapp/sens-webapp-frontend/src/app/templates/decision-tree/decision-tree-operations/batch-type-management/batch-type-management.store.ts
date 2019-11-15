import { Injectable } from '@angular/core';
import { Set } from 'immutable';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { BatchType, BatchTypes, BatchTypeUpdates } from './batch-type-management.model';
import { BatchTypeManagementService } from './batch-type-management.service';

@Injectable()
export class BatchTypeManagementStore {

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

  constructor(private assignedToService: BatchTypeManagementService) { }

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
    this._activatedBatchTypes.next(this._activatedBatchTypes.getValue().remove(toAssign));
  }

  activate(toActivate: BatchType) {
    const hasNotBeenDeactivated = !this.toDeactivate.has(toActivate);
    if (hasNotBeenDeactivated) {
      if (this.isAvaiable(toActivate)) {
        this.toAssign.add(toActivate);
      }
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
    this._availableBatchTypes.next(this._availableBatchTypes.getValue().remove(toActivate));
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

  deactivate(toDeactivate: BatchType, moveTo: 'assign' | 'available') {
    const hasNotBeenActivated = !this.toActivate.has(toDeactivate);
    if (hasNotBeenActivated) {
      this.toDeactivate.add(toDeactivate);
      if (moveTo === 'available') {
        this.toUnassign.add(toDeactivate);
      }
    } else {
      this.toActivate.remove(toDeactivate);
    }

    this._activatedBatchTypes.next(this._activatedBatchTypes.getValue().remove(toDeactivate));

    if (moveTo === 'assign') {
      this._assignedBatchTypes.next(this._assignedBatchTypes.getValue().add(toDeactivate));
    } else if (moveTo === 'available') {
      this._availableBatchTypes.next(this._availableBatchTypes.getValue().add(toDeactivate));
    }
  }

  isAssigned(batch: BatchType): boolean {
    return this._assignedBatchTypes.value.has(batch);
  }

  isAvaiable(batch: BatchType): boolean {
    return this._availableBatchTypes.value.has(batch);
  }

  isActive(batch: BatchType): boolean {
    return this._activatedBatchTypes.value.has(batch);
  }

  private clearUpdates() {
    this.toActivate.clear();
    this.toDeactivate.clear();
    this.toAssign.clear();
    this.toUnassign.clear();
  }

  private updateBatchTypesStoreState(batchTypes: BatchTypes) {
    const activatedBatchTypes = batchTypes.activated;
    const assignedBatchTypes = batchTypes.assigned;
    const availableBatchTypes = batchTypes.available;

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
