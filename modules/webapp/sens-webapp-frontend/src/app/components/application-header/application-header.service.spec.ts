import { TestBed, async, getTestBed } from '@angular/core/testing';

import { ApplicationHeaderService } from './application-header.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { ApplicationInfo } from './models/application-info';
import { environment } from '../../../environments/environment';

export const mockResponse: ApplicationInfo = {
  'git': {
    'build': {
      'version': '2.4.10-SNAPSHOT',
    }
  }
};

describe('ApplicationHeaderService', () => {
  let injector: TestBed;
  let service: ApplicationHeaderService;
  let httpMock: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ApplicationHeaderService]
    });

    injector = getTestBed();
    service = TestBed.get(ApplicationHeaderService);
    httpMock = injector.get(HttpTestingController);
  }));

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('#getAppInfo', () => {
    it('should return an Observable<ApplicationInfo>', () => {
      service.getAppInfo().subscribe(data => {
        expect(data).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${environment.serverApiUrl}management/info`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });
});
