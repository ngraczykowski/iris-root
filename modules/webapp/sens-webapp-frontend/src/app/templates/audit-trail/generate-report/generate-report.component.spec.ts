import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { WINDOW } from '../../../shared/window.service';
import { TestModule } from '../../../test/test.module';
import { AuditTrailModule } from '../audit-trail.module';

import { GenerateReportComponent } from './generate-report.component';

const MockWindow = {
  location: {
    assign(param) { }
  }
};

describe('GenerateReportComponent', () => {
  let component: GenerateReportComponent;
  let fixture: ComponentFixture<GenerateReportComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, AuditTrailModule],
          providers: [
            {
              provide: WINDOW, useValue: MockWindow
            }
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call open with given url, when onGenerateReport invoked', () => {
    const spy = spyOn(component.window.location, 'assign');
    component.reportType = 'type';
    component.reportUrl = 'url';

    component.onGenerateReport();

    expect(spy).toHaveBeenCalledWith(component.reportUrl);
  });
});
