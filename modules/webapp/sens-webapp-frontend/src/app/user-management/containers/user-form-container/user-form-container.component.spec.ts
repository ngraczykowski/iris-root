import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserFormContainerComponent } from './user-form-container.component';

describe('UserFormComponent', () => {
  let component: UserFormContainerComponent;
  let fixture: ComponentFixture<UserFormContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserFormContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserFormContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
