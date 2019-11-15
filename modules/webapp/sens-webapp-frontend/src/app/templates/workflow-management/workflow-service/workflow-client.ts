import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { WorkflowChange, WorkflowDetails, WorkflowList } from '../../model/workflow.model';

@Injectable()
export class WorkflowClient {

  constructor(private http: HttpClient) {}

  getWorkflows(): Observable<WorkflowList> {
    return this.http.get(`${environment.serverApiUrl}api/workflows`)
        .pipe(map(body => <WorkflowList> body));
  }

  getWorkflow(decisionTreeId: number): Observable<WorkflowDetails> {
    return this.http.get(`${environment.serverApiUrl}api/workflow/${decisionTreeId}`)
        .pipe(map(body => <WorkflowDetails> body));
  }

  save(workflow: WorkflowChange): Observable<any> {
    return this.http.post(`${environment.serverApiUrl}api/workflow/${workflow.decisionTreeId}`, workflow);
  }
}
