import { Injectable } from '@angular/core';
import { ChangeRequestEditFormService } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-edit-form.service';
import {
  ChangeRequest,
  ProposeChangesResponse
} from '@app/templates/decision-tree/branch-change/branch-updater/change-request.model';
import { CreateChangeRequestClient } from '@app/templates/decision-tree/branch-change/branch-updater/create-change-request-client.service';
import { Branch } from '@app/templates/model/branch.model';
import { Observable } from 'rxjs';

@Injectable()
export class BranchUpdaterService {

  constructor(
      private formService: ChangeRequestEditFormService,
      private createRequestClient: CreateChangeRequestClient
  ) { }

  proposeChanges(decisionTreeId: number, branches: Branch[]): Observable<ProposeChangesResponse> {
    const request = this.buildRequest(decisionTreeId, branches);
    return this.createRequestClient.createChanges(request);
  }

  private buildRequest(decisionTreeId: number, branches: Branch[]): ChangeRequest {
    return {
      decisionTreeId: decisionTreeId,
      matchGroupIds: branches.map(b => b.matchGroupId),
      solution: this.formService.getSolutionChange(),
      status: this.formService.getStatusChange(),
      comment: this.formService.getComment()
    };
  }

  resetChangeForm() {
    this.formService.reset();
  }

  isChangeRequestReady() {
    return this.formService.isInitialized() && this.formService.isValid() && this.formService.isDirty();
  }
}
