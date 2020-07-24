import { Injectable } from '@angular/core';
import {
  ReasoningBranchesListResponse
} from '@app/reasoning-branches-browser/model/branches-list';
import { ReasoningBranchesEndpointService } from '@endpoint/reasoning-branches/services/reasoning-branches-endpoint.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReasoningBranchesService {

  constructor(
      private reasoningBranchesEndpointService: ReasoningBranchesEndpointService) { }

  public getList(params): Observable<ReasoningBranchesListResponse> {
    return this.reasoningBranchesEndpointService.get(params);
  }

}
