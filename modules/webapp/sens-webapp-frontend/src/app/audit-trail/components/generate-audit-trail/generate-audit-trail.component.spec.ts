import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateAuditTrailComponent } from './generate-audit-trail.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('GenerateAuditTrailComponent', () => {
  let component: GenerateAuditTrailComponent;
  let fixture: ComponentFixture<GenerateAuditTrailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GenerateAuditTrailComponent ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateAuditTrailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
