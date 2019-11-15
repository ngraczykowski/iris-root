import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';
import { UserNameViewComponent, UserNameViewData } from './user-name-view.component';


describe('TextViewComponent', () => {
  let component: UserNameViewComponent;
  let fixture: ComponentFixture<UserNameViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule],
          declarations: [UserNameViewComponent]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserNameViewComponent);
    component = fixture.componentInstance;
    component.data = <UserNameViewData> {};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
