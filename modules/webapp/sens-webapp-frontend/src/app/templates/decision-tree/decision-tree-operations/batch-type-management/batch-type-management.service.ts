import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../../../environments/environment';
import { BatchTypesDTO, BatchTypesUpdateDTO } from '../../../model/batch-type.model';
import { BatchType, BatchTypes, BatchTypeUpdates } from './batch-type-management.model';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class BatchTypeManagementService {
  checkedElements = new BehaviorSubject([]);
  showUsedBatches = new BehaviorSubject(true);

  constructor(private http: HttpClient) { }

  private static mapBatchTypeUpdatesToRequestDTO(updates: BatchTypeUpdates): BatchTypesUpdateDTO {
    return {
      toAssign: updates.toAssign.map(batchType => batchType.name),
      toActivate: updates.toActivate.map(batchType => batchType.name),
      toDeactivate: updates.toDeactivate.map(batchType => batchType.name),
      toUnassign: updates.toUnassign.map(batchType => batchType.name)
    } as BatchTypesUpdateDTO;
  }

  getBatchTypesForTree(decisionTreeId: number): Observable<BatchTypes> {
    const request = this.http.get(
        environment.serverApiUrl + 'api/decision-tree/' + decisionTreeId + '/assignments',
        {
          observe: 'response'
        });

    return request
        .map((res: HttpResponse<any>) => res.body as BatchTypesDTO)
        .map((batchTypesDto) => this.mapResponseToBatchTypes(batchTypesDto));
  }

  updateBatchTypesForTree(decisionTreeId, updates: BatchTypeUpdates): Observable<any> {
    return this.http.post(
        environment.serverApiUrl + 'api/decision-tree/' + decisionTreeId + '/assignments',
        BatchTypeManagementService.mapBatchTypeUpdatesToRequestDTO(updates),
        {
          observe: 'response',
        });
  }

  setCheckedElements(element: BatchType) {
    const elementIndex = this.checkedElements.value.findIndex(obj => obj.name === element.name);
    if (elementIndex === -1) {
      this.checkedElements.next([...this.checkedElements.value, element]);
    }
  }

  removeCheckedElement(element: BatchType) {
    const elementIndex = this.checkedElements.value.findIndex(obj => obj.name === element.name);
    if (elementIndex > -1) {
      this.checkedElements.value.splice(elementIndex, 1);
      this.checkedElements.next(this.checkedElements.value);
    }
  }

  uncheckAll() {
    this.checkedElements.next([]);
  }

  uncheckNotMatchingFilterQuery(query) {
    if (this.checkedElements.value.length) {
      const elementIndex = this.checkedElements.value.findIndex(obj => !obj.name.toLowerCase().includes(query.toLowerCase()));
      this.checkedElements.value.splice(elementIndex, 1);
      this.checkedElements.next(this.checkedElements.value);
    }
  }

  isChecked(element) {
    return this.checkedElements.value.findIndex(obj => obj.name === element.name) > -1;
  }

  toggleUsedBatches() {
    this.showUsedBatches.next(!this.showUsedBatches.value);
  }

  resetUsedBatches() {
    this.showUsedBatches.next(true);
  }

  onDiscardChanges(): void {
    this.uncheckAll();
    this.resetUsedBatches();
  }

  private mapResponseToBatchTypes(batchTypesDto: BatchTypesDTO): BatchTypes {
    return {
      activated: batchTypesDto.activated.map((batchType) => <BatchType> {
        name: batchType,
        canActivate: true
      }),
      assigned: this.getAssignedBatches(batchTypesDto),
      available: this.getAvailableBatches(batchTypesDto)
    } as BatchTypes;
  }

  private getAssignedBatches(batchTypes): BatchType[] {
    const available = batchTypes.available.map(batches => batches.batchType);
    const toReturn: BatchType[] = [];
    batchTypes.assigned.map(batch => {
      const index = available.indexOf(batch);
      if (index > -1) {
        toReturn.push(this.mapToBatchType(batchTypes.available[index]));
      }
    });

    return toReturn.filter(batch => this.nameIsNotIn(batch, batchTypes.activated));
  }

  private getAvailableBatches(batchTypes): BatchType[] {
    return batchTypes.available.map((batchType) => this.mapToBatchType(batchType))
          .filter(
            batchType => this.nameIsNotIn(batchType, batchTypes.assigned)
                      && this.nameIsNotIn(batchType, batchTypes.activated)
          );
  }

  private mapToBatchType(object): BatchType {
    return {
      name: object.batchType,
      canActivate: object.canActivate,
      activeForId: object.decisionTreeId,
      activeForName: object.decisionTreeName
    };
  }

  private nameIsNotIn(batchType: BatchType, array: string[]) {
    return array.indexOf(batchType.name) === -1;
  }
}
