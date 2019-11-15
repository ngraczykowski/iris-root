import { Injectable } from '@angular/core';
import { ChangeRequestRejectFormService } from '@app/templates/decision-tree/branch-change/branch-reject/change-request-reject-form/change-request-reject-form.service';
import { RejectChangeRequestClient } from '@app/templates/decision-tree/branch-change/branch-reject/reject-change-request-client.service';
import { ChangeRequest } from '@app/templates/decision-tree/branch-change/branch-reject/reject-request.model';
import { Branch } from '@app/templates/model/branch.model';
import { Observable } from 'rxjs';

@Injectable()
export class BranchRejectService {

  constructor(
      private formService: ChangeRequestRejectFormService,
      private rejectRequestClient: RejectChangeRequestClient
  ) { }

  rejectChange(decisionTreeId: number, branches: Branch[]): Observable<any> {
    const request = this.buildRequest(decisionTreeId, branches);
    return this.rejectRequestClient.rejectChange(request);
  }

  private buildRequest(decisionTreeId: number, branches: Branch[]): ChangeRequest {
    return {
      decisionTreeId: decisionTreeId,
      matchGroupIds: branches.map(b => b.matchGroupId),
      comment: this.formService.getComment()
    };
  }

  isChangeRequestReady() {
    return this.formService.isInitialized() && this.formService.isValid();
  }
}
