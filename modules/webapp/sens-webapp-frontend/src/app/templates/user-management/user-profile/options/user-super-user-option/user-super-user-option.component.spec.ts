import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl } from '@angular/forms';
import { TestModule } from '../../../../../test/test.module';
import { UserOptionsModule } from '../user-options.module';

import { UserSuperUserOptionComponent } from './user-super-user-option.component';

describe('UserSuperuserOptionComponent', () => {
  let component: UserSuperUserOptionComponent;
  let fixture: ComponentFixture<UserSuperUserOptionComponent>;

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
    fixture = TestBed.createComponent(UserSuperUserOptionComponent);
    component = fixture.componentInstance;
    component.control = new FormControl(false);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
