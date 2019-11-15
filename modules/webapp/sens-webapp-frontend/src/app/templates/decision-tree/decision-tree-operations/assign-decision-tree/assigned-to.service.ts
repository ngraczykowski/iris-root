import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../../../environments/environment';
import { BatchTypesDTO, BatchTypesUpdateDTO } from '../../../model/batch-type.model';
import { BatchType, BatchTypes, BatchTypeUpdates } from './assigned-to.model';

@Injectable()
export class AssignedToService {

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
        AssignedToService.mapBatchTypeUpdatesToRequestDTO(updates),
        {
          observe: 'response',
        });
  }


  private mapResponseToBatchTypes(batchTypesDto: BatchTypesDTO): BatchTypes {
    return {
      activated: batchTypesDto.activated.map((batchType) => <BatchType> {
        name: batchType,
        canActivate: true
      }),
      assigned: batchTypesDto.assigned.map((batchType) => <BatchType> {
        name: batchType,
        canActivate: true
      }),
      available: batchTypesDto.available.map((batchType) => <BatchType> {
        name: batchType.batchType,
        canActivate: batchType.canActivate
      })
    } as BatchTypes;
  }
}
