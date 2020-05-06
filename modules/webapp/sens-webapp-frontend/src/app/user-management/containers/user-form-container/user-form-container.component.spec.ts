import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserFormContainerComponent } from './user-form-container.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { UserManagementService } from '@app/user-management/services/user-management.service';
import { of } from 'rxjs';
import { LocalEventService } from '@app/shared/event/local-event.service';

describe('UserFormComponent', () => {
  let component: UserFormContainerComponent;
  let fixture: ComponentFixture<UserFormContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserFormContainerComponent ],
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
    fixture = TestBed.createComponent(UserFormContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
