import { TestBed } from '@angular/core/testing';

import { ExternalAppsService } from './external-apps.service';

describe('ExternalAppsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ExternalAppsService = TestBed.get(ExternalAppsService);
    expect(service).toBeTruthy();
  });
});
