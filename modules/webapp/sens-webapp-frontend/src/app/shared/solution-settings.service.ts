import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Solution } from '@app/components/solution-tag/solution-tag.component';
import { environment } from '@env/environment';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SolutionSettingsService {
  settings = new BehaviorSubject(null);

  constructor(private http: HttpClient) { }

  getSolutionSettings() {
    this.http.get(`${environment.serverApiUrl}api/settings/decisions`).subscribe(data => {
      this.settings.next(data);
    });
  }

  getSolutionLabel(solution: string) {
    let solutionLabel;

    this.settings.subscribe((data: Solution[]) => {
      if (data !== null) {
        solutionLabel = data.find(i => i.key === solution).label;
      }
    });

    return solutionLabel;
  }
}
