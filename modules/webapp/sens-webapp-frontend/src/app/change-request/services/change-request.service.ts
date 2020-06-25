import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { BehaviorSubject, forkJoin, Observable } from 'rxjs';
import { v4 as uuidv4 } from 'uuid';
import { ChangeRequest } from '../models/change-request';
import {
  FeatureVectorSignaturesPayload,
  FeatureVectorSignaturesResponse
} from '../models/feature-vector-signatures';
import { ValidateBranchIdsPayload, ValidateBranchIdsResponse } from '../models/validate-branch-ids';

@Injectable({
  providedIn: 'root'
})
export class ChangeRequestService {
  decisionTreeId$ = new BehaviorSubject(null);
  reasoningBranchesCount$ = new BehaviorSubject(0);
  changeRequestData$ = new BehaviorSubject<ChangeRequest>({
    id: null,
    bulkChangeId: null,
    comment: null,
    createdAt: null
  });

  correlationIdHeader = {
    CorrelationId: uuidv4()
  };

  whiteChar = '\n';

  constructor(
      private http: HttpClient
  ) { }

  validateBranches(payload: ValidateBranchIdsPayload): Observable<ValidateBranchIdsResponse> {
    this.decisionTreeId$.next(payload.decisionTreeId);
    this.reasoningBranchesCount$.next(payload.branchIds.length);

    return this.http.put<ValidateBranchIdsResponse>(
        `${environment.serverApiUrl}/decision-trees/${payload.decisionTreeId}/branches/validate`,
        payload
    );
  }

  validateFeatureVectorSignatures(payload: FeatureVectorSignaturesPayload): Observable<FeatureVectorSignaturesResponse> {
    this.decisionTreeId$.next(payload.decisionTreeId);
    this.reasoningBranchesCount$.next(payload.featureVectorSignatures.length);

    return this.http.put<FeatureVectorSignaturesResponse>(
        `${environment.serverApiUrl}/decision-trees/${payload.decisionTreeId}/branches/validate`,
        payload
    );
  }

  setReasoningBranchIdsData({branchIds}: ValidateBranchIdsResponse) {
    this.changeRequestData$.next({
      ...this.changeRequestData$.getValue(),
      reasoningBranchIds: branchIds.map(value => ({
        decisionTreeId: this.decisionTreeId$.getValue(),
        featureVectorId: value.branchId
      }))
    });
  }

  setBulkChangeId() {
    const id = uuidv4();
    this.changeRequestData$.next({
      ...this.changeRequestData$.getValue(),
      id: id,
      bulkChangeId: id
    });
  }

  setCreatedAtDate() {
    this.changeRequestData$.next({
      ...this.changeRequestData$.getValue(),
      createdAt: new Date().toISOString()
    });
  }

  setComment(comment) {
    this.changeRequestData$.next({
      ...this.changeRequestData$.getValue(),
      comment: comment
    });
  }

  setAiSolution(decision) {
    if (decision !== 'Leave unchanged') {
      this.changeRequestData$.next({
        ...this.changeRequestData$.getValue(),
        aiSolution: decision
      });
    }
  }

  setAiStatus(status) {
    if (status !== 'Leave unchanged') {
      this.changeRequestData$.next({
        ...this.changeRequestData$.getValue(),
        active: status
      });
    }
  }

  registerChangeRequest() {
    this.setCreatedAtDate();
    return forkJoin(
        this.bulkChanges(this.changeRequestData$.getValue()),
        this.changeRequests(this.changeRequestData$.getValue())
    );
  }

  bulkChanges(payload: ChangeRequest) {
    return this.http.post(`${environment.serverApiUrl}/bulk-changes`, payload, {
      headers: {
        ...this.correlationIdHeader
      }
    });
  }

  changeRequests(payload: ChangeRequest) {
    return this.http.post(`${environment.serverApiUrl}/change-requests/pending`, payload, {
      headers: {
        ...this.correlationIdHeader
      }
    });
  }

  parseBranchIds(payload: string) {
    return this.removeEmptyElements(
        payload.split(this.whiteChar).map(id => parseInt(id, 0))
    );
  }

  parseFeatureVectorSignatures(payload: string) {
    return this.removeEmptyElements(
        payload.split(this.whiteChar)
    );
  }

  removeEmptyElements(array) {
    return array.filter(Boolean);
  }
}
