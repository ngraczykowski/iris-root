import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { RestrictionsData } from './restriction';

@Injectable({
  providedIn: 'root'
})
export class AlertRestrictionsManagementService {
  restrictions = new BehaviorSubject<RestrictionsData>(null);

  constructor( private http: HttpClient ) { }

  public change: EventEmitter<any> = new EventEmitter();

  getRestrictions(): Observable<RestrictionsData> {
    return this.http.get<RestrictionsData>(`${environment.serverApiUrl}api/restrictions`);
  }

  get() {
    this.getRestrictions().subscribe(data => this.restrictions.next(data));
  }

  restrictionPanelData(value) {
    this.change.emit(value);
  }

  delete(id) {
    return this.http.delete(`${environment.serverApiUrl}api/restriction/${id}`);
  }
}
