import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DownloadAuditTrailComponent } from './download-audit-trail.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('DownloadAuditTrailComponent', () => {
  let component: DownloadAuditTrailComponent;
  let fixture: ComponentFixture<DownloadAuditTrailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DownloadAuditTrailComponent ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DownloadAuditTrailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
