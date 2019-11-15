import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { AuthService } from '../../../shared/auth/auth.service';
import { TestModule } from '../../../test/test.module';
import { ApplicationHeaderModule } from '../application-header.module';
import { ApplicationHeaderService } from '../application-header.service';
import { mockResponse } from '../application-header.service.spec';
import { ApplicationInfo } from '../models/application-info';

import { UserSectionComponent } from './user-section.component';

describe('UserSectionComponent', () => {
  let applicationHeaderService: ApplicationHeaderService;
  let component: UserSectionComponent;
  let fixture: ComponentFixture<UserSectionComponent>;

  let authService: AuthService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, ApplicationHeaderModule],
          providers: [
            ApplicationHeaderService
          ]
        })
        .compileComponents();

    authService = TestBed.get(AuthService);
    applicationHeaderService = TestBed.get(ApplicationHeaderService);
    spyOn(authService, 'getDisplayName').and.returnValue('displayName');
    spyOn(authService, 'getUserName').and.returnValue('userName');
  }));

  function initComponent() {
    fixture = TestBed.createComponent(UserSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  it('should call for getAppInfo() and return application data', () => {
    const response: ApplicationInfo = mockResponse;
    spyOn(applicationHeaderService, 'getAppInfo').and.returnValue(of(response));

    initComponent();
    expect(component.applicationVersion).toEqual(response.build.version);
  });
});
