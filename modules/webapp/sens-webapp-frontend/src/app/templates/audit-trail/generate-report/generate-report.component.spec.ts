import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { WINDOW } from '../../../shared/window.service';
import { TestModule } from '../../../test/test.module';
import { SecurityMatrixModule } from '../audit-trail.module';

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
          imports: [TestModule, SecurityMatrixModule],
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

});
