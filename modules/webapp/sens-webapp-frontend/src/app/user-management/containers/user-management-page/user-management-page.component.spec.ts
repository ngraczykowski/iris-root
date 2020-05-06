import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserManagementPageComponent } from './user-management-page.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { UserManagementService } from '@app/user-management/services/user-management.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { of } from 'rxjs';

describe('UserManagementPageComponent', () => {
  let component: UserManagementPageComponent;
  let fixture: ComponentFixture<UserManagementPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserManagementPageComponent ],
      imports: [ HttpClientTestingModule ],
      providers: [
        {
          provide: UserManagementService,
          useValue: {
            getUsers: function () { return of(); }
          }
        },
        LocalEventService
      ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserManagementPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
