import { TestBed } from '@angular/core/testing';

import { SolutionSettingsService } from './solution-settings.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '@env/environment';

describe('SolutionSettingsService', () => {
  let httpTestingController: HttpTestingController;
  let service: SolutionSettingsService;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SolutionSettingsService],
      imports: [HttpClientTestingModule]
    });

    httpTestingController = TestBed.get(HttpTestingController);
    service = TestBed.get(SolutionSettingsService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('#getSolutionSettings()', () => {
    it('should make expected http call', () => {
      service.getSolutionSettings();

      const req = httpTestingController.expectOne(`${environment.serverApiUrl}api/settings/decisions`);
      expect(req.request.method).toBe('GET');
    });
  });
});
