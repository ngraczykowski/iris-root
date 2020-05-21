import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureAuditTrailComponent } from './configure-audit-trail.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('ConfigureAuditTrailComponent', () => {
  let component: ConfigureAuditTrailComponent;
  let fixture: ComponentFixture<ConfigureAuditTrailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureAuditTrailComponent ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureAuditTrailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
