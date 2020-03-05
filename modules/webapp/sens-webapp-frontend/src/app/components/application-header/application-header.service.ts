import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationInfo } from './models/application-info';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class ApplicationHeaderService {

  constructor(
    private http: HttpClient
  ) { }

  getAppInfo(): Observable<ApplicationInfo> {
    return this.http.get<ApplicationInfo>(`${environment.managementApiUrl}/info`);
  }
}
