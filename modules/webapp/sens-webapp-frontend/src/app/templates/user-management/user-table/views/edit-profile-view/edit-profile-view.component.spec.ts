import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';

import { EditProfileViewComponent, EditProfileViewData } from './edit-profile-view.component';

describe('EditProfileViewComponent', () => {
  let component: EditProfileViewComponent;
  let fixture: ComponentFixture<EditProfileViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule],
          declarations: [EditProfileViewComponent]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditProfileViewComponent);
    component = fixture.componentInstance;
    component.data = <EditProfileViewData> {};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
