import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ChangelogView } from './changelog.model';

@Injectable()
export class ChangelogClient {

  constructor(private http: HttpClient) { }

  getChangelog(decisionTreeId: number, matchGroupId: number): Observable<ChangelogView> {
    return this.http
        .get(environment.serverApiUrl + `api/changelog/${decisionTreeId}/branch/${matchGroupId}`)
        .map((body) => <ChangelogView> body);
  }
}
