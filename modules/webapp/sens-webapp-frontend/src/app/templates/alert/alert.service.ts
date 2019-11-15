import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AlertDetails } from '../model/alert.model';
import { MatchesPage, MatchPageResponse } from '../model/match.model';

class IllegalAlertDetailsParamsException extends Error {

}

@Injectable()
export class AlertService {

  constructor(private http: HttpClient) { }

  getAlertDetails(params): Observable<AlertDetails> {
    return this.http.get(environment.serverApiUrl + this.resolveUrl(params))
        .pipe(map((body) => new AlertDetails().deserialize(body)));
  }

  private resolveUrl(params): string {
    if (this.exists(params.decisionTreeId) && this.exists(params.matchGroupId) && this.exists(params.externalId)) {
      return 'api/decision-tree/' + params.decisionTreeId + '/branch/' + params.matchGroupId + '/alert/' + params.externalId;
    } else if (this.exists(params.decisionTreeId) && this.exists(params.externalId)) {
      return 'api/decision-tree/' + params.decisionTreeId + '/alert/' + params.externalId;
    } else if (this.exists(params.alertIdOrExternalId)) {
      return 'api/alert/' + params.alertIdOrExternalId;
    } else {
      throw new IllegalAlertDetailsParamsException();
    }
  }

  private exists(value): boolean {
    return value !== undefined && value !== null;
  }

  getMatchesByAlertNotInMatchGroup(page, size, alertId, matchGroupId): Observable<MatchesPage> {
    return this.getMatchesByAlertAndMatchGroup(page, size, alertId, matchGroupId, 'NOT_IN');
  }

  getMatchesByAlertInMatchGroup(page, size, alertId, matchGroupId): Observable<MatchesPage> {
    return this.getMatchesByAlertAndMatchGroup(page, size, alertId, matchGroupId, 'IN');
  }

  private getMatchesByAlertAndMatchGroup(page, size, alertId, matchGroupId, matchGroupRelationship): Observable<MatchesPage> {
    const toBackendPaging = (pageNumber) => String(pageNumber - 1);

    return this.http.get(environment.serverApiUrl + 'api/alert/' + alertId + '/matches',
        {
          params: {
            page: toBackendPaging(page),
            size,
            matchGroupId,
            matchGroupRelationship
          }
        })
        .pipe(
            map((body) => <MatchPageResponse>body),
            map((matchPageResponse: MatchPageResponse) => matchPageResponse.matches)
        );
  }
}
