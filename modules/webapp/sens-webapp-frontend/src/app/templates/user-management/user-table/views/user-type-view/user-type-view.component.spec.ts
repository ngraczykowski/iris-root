import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';

import { UserTypeViewComponent, UserTypeViewData } from './user-type-view.component';

describe('UserTypeViewComponent', () => {
  let component: UserTypeViewComponent;
  let fixture: ComponentFixture<UserTypeViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule],
          declarations: [UserTypeViewComponent]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserTypeViewComponent);
    component = fixture.componentInstance;
    component.data = <UserTypeViewData> {};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
