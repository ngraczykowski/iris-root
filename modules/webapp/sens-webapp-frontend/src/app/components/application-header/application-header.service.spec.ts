import { TestBed, async, getTestBed } from '@angular/core/testing';

import { ApplicationHeaderService } from './application-header.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { ApplicationInfo } from './models/application-info';
import { environment } from '../../../environments/environment';

export const mockResponse: ApplicationInfo = {
  'component': 'SENS Web App REST API',
  'git': {
    'commit': {
      'time': '2019-01-09T09:13:08.000+0000',
      'id': '055ee0f'
    },
    'branch': 'release-2.4'
  },
  'build': {
    'version': '2.4.10-SNAPSHOT',
    'artifact': 'sens-web-app',
    'name': 'SENS Web App',
    'group': 'com.silenteight.sens',
    'time': '2019-01-09T11:55:18.000+0000'
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
