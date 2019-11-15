import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, ValidationErrors } from '@angular/forms';
import { TestModule } from '../../test/test.module';

import { InputControlFeedbackComponent } from './input-control-feedback.component';

describe('InputControlFeedbackComponent', () => {
  let component: InputControlFeedbackComponent;
  let fixture: ComponentFixture<InputControlFeedbackComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InputControlFeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return empty errors if no control', () => {
    expect(component.getErrors()).toEqual([]);
  });

  it('should return empty errors if no errors', () => {
    component.control = new FormControl('');

    expect(component.getErrors()).toEqual([]);
  });

  it('should return valid errors if there are some error keys', () => {
    component.translatePrefix = 'prefix';
    component.control = new FormControl('');
    component.control.setErrors(<ValidationErrors> {
      error1: true,
      error2: false,
      error3: true
    });

    expect(component.getErrors()).toEqual(['prefix.error1', 'prefix.error3']);
  });
});
