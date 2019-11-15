import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';
import { UserOptionsModule } from '../user-options.module';
import { UserPasswordOptionComponent } from './user-password-option.component';

describe('UserPasswordOptionComponent', () => {
  let component: UserPasswordOptionComponent;
  let fixture: ComponentFixture<UserPasswordOptionComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            UserOptionsModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserPasswordOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
