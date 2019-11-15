import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { CollectionResponse } from '../model/collection-response.model';
import { InboxMessage, InboxMessageRequest, InboxStatistics } from '../model/inbox.model';

@Injectable()
export class InboxService {

  private static buildParams(type, referenceId) {
    return {
      type: type,
      referenceId: referenceId
    };
  }

  constructor(private http: HttpClient) {
  }

  getStats(): Observable<InboxStatistics> {
    return this.http.get(
        environment.serverApiUrl + 'api/inbox/stats')
        .pipe(map(body => <InboxStatistics>body));
  }

  getInboxMessage(type: string, referenceId: string): Observable<InboxMessage> {
    return this.http.get(
        `${environment.serverApiUrl}api/inbox/message`,
        {params: InboxService.buildParams(type, referenceId)}
    ).pipe(map(body => <InboxMessage> body));
  }

  markAsSolved(inboxMessageId: number): Observable<any> {
    return this.http.post(
        `${environment.serverApiUrl}api/inbox/message/solve`,
        {inboxMessageId: inboxMessageId}
    );
  }

  getSolvedMessages(page: number, size: number): Observable<CollectionResponse<InboxMessage>> {
    return this.getMessages({
      state: 'SOLVED',
      page: page,
      size: size
    });
  }

  getUnsolvedMessages(page: number, size: number): Observable<CollectionResponse<InboxMessage>> {
    return this.getMessages({
      state: 'UNSOLVED',
      page: page,
      size: size
    });
  }

  getMessages(request: InboxMessageRequest): Observable<CollectionResponse<InboxMessage>> {
    const state = request.state;
    const pageIndex = request.page - 1;
    const size = request.size;

    return this._getMessages({state: state, page: pageIndex, size: size});
  }

  private _getMessages(params): Observable<CollectionResponse<InboxMessage>> {
    return this.http.get(environment.serverApiUrl + 'api/inbox/messages',
        {params: params})
        .pipe(map(body => <CollectionResponse<InboxMessage>> body));
  }
}
