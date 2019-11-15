import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';
import { UserOptionsModule } from '../user-options.module';
import { UserStatusOptionComponent } from './user-status-option.component';

describe('UserStatusOptionComponent', () => {
  let component: UserStatusOptionComponent;
  let fixture: ComponentFixture<UserStatusOptionComponent>;

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
    fixture = TestBed.createComponent(UserStatusOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
