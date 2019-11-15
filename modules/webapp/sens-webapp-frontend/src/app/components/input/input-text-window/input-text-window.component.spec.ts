import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { InputValidationComponent } from '@app/components/input/input-validation/input-validation.component';
import { TranslateModule } from '@ngx-translate/core';
import { InputTextWindowComponent } from './input-text-window.component';

describe('InputTextWindowComponent', () => {
  let component: InputTextWindowComponent;
  let fixture: ComponentFixture<InputTextWindowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslateModule,
        FormsModule
      ],
      declarations: [
        InputTextWindowComponent,
        InputValidationComponent
      ]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InputTextWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
