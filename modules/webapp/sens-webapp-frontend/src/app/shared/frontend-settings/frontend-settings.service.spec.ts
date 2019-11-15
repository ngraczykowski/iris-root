import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, TestBed, } from '@angular/core/testing';
import { FrontendSettings } from '@app/shared/frontend-settings/frontend-settings.model';
import { environment } from '@env/environment.prod';
import { FrontendSettingsService } from './frontend-settings.service';

describe('FrontendSettingsService', () => {
  let service: FrontendSettingsService;
  let httpMock: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FrontendSettingsService]
    }).compileComponents();

    service = TestBed.get(FrontendSettingsService);
    httpMock = TestBed.get(HttpTestingController);
  }));

  afterEach(() => {
    httpMock.verify();
  });
});
