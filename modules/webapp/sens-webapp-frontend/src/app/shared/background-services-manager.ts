import { InjectionToken } from '@angular/core';

export const BackgroundServiceToken = new InjectionToken<BackgroundService>('backgroundService');

export interface BackgroundService {

  startService(): void;

  stopService(): void;
}

export class BackgroundServicesManager implements BackgroundService {

  constructor(private services: Array<BackgroundService> = []) {
  }

  startService() {
    this.services.forEach(s => {
      s.startService();
      console.log('Started service: ' + s.constructor.name);
    });
  }

  stopService() {
    this.services.forEach(s => {
      s.stopService();
      console.log('Stopped service: ' + s.constructor.name);
    });
  }
}
