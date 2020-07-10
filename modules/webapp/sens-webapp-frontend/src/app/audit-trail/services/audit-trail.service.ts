import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { GenerateReportPayload } from '../models/generate-report-payload';
import { tap, map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { AuditTrailResponse } from '../models/audit-report-response';

@Injectable({
  providedIn: 'root'
})
export class AuditTrailService {

  constructor(
    private http: HttpClient
  ) { }

  getReport(payload: GenerateReportPayload): Observable<AuditTrailResponse> {
    return this.http.get(
      `${environment.serverApiUrl}/reports/audit-report?from=${payload.startDate}&to=${payload.endDate}`,
      {
        responseType: 'text',
        observe: 'response'
      }
    ).pipe(
      map(response => ({
          file: response.body,
          filename: this.getFilename(response.headers.get('Content-Disposition'))
        })
      )
    );
  }

  getFilename(header): string {
    const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
    return filenameRegex.exec(header)[1];
  }
}
