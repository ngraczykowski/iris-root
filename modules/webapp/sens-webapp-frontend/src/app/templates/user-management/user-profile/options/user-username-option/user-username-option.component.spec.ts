import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl } from '@angular/forms';
import { TestModule } from '../../../../../test/test.module';
import { UserOptionsModule } from '../user-options.module';
import { UserUsernameOptionComponent } from './user-username-option.component';

describe('UserDisplayNameOptionComponent', () => {
  let component: UserUsernameOptionComponent;
  let fixture: ComponentFixture<UserUsernameOptionComponent>;

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
    fixture = TestBed.createComponent(UserUsernameOptionComponent);
    component = fixture.componentInstance;
    component.control = new FormControl('Username');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
