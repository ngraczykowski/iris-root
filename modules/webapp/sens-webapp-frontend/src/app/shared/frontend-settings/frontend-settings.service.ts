import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';

import { environment } from '../../../environments/environment.prod';
import { FrontendSettings } from './frontend-settings.model';

@Injectable()
export class FrontendSettingsService {

  private settings: FrontendSettings;
  private settingsObservable: Observable<FrontendSettings>;

  constructor(private http: HttpClient) {
  }

  getSettings(): Observable<FrontendSettings> {
    if (this.settings) {
      return of(this.settings);
    } else {
      return this.fetchAndCacheSettings();
    }
  }

  private fetchAndCacheSettings() {
    return this.fetchSettings().pipe(
        tap(settings => this.settings = settings)
    );
  }

  private fetchSettings(): Observable<FrontendSettings> {
    if (!this.settingsObservable) {
      this.settingsObservable =
          this.http.get(environment.serverApiUrl + 'api/settings/frontend')
              .pipe(
                  map(body => <FrontendSettings> body),
              );
    }
    return this.settingsObservable;
  }
}

