import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuditTrailComponent } from './audit-trail.component';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { TranslateModule } from '@ngx-translate/core';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { TestModule } from '@app/test/test.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('AuditTrailComponent', () => {
  let component: AuditTrailComponent;
  let fixture: ComponentFixture<AuditTrailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuditTrailComponent ],
      imports: [
        CommonModule,
        MatButtonModule,
        MatStepperModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatProgressSpinnerModule,
        UiComponentsModule,
        TranslateModule,
        BrowserAnimationsModule,
        TestModule
      ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuditTrailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
