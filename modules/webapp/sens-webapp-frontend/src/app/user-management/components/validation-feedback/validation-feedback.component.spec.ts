import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidationFeedbackComponent } from './validation-feedback.component';
import { TestModule } from '@app/test/test.module';

describe('ValidationFeedbackComponent', () => {
  let component: ValidationFeedbackComponent;
  let fixture: ComponentFixture<ValidationFeedbackComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ValidationFeedbackComponent ],
      imports: [ TestModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidationFeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
