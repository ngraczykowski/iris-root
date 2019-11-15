import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';
import { UserOptionsModule } from '../user-options.module';
import { UserRolesOptionComponent } from './user-roles-option.component';

describe('UserRolesOptionComponent', () => {
  let component: UserRolesOptionComponent;
  let fixture: ComponentFixture<UserRolesOptionComponent>;

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
    fixture = TestBed.createComponent(UserRolesOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
