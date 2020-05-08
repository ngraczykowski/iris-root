import { TestBed, async, getTestBed } from '@angular/core/testing';

import { AppBarService } from './app-bar.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { environment } from '@env/environment';
import { ApplicationInfo } from '../models/application-info';

export const mockResponse: ApplicationInfo = {
  'git': {
    'build': {
      'version': '2.4.10-SNAPSHOT',
    }
  }
};

describe('ApplicationHeaderService', () => {
  let injector: TestBed;
  let service: AppBarService;
  let httpMock: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AppBarService]
    });

    injector = getTestBed();
    service = TestBed.get(AppBarService);
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

      const req = httpMock.expectOne(`${environment.managementApiUrl}/info`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });
});
