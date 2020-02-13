import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReasoningBranchManagementPageComponent } from './reasoning-branch-management-page.component';

describe('ReasoningBranchManagementPageComponent', () => {
  let component: ReasoningBranchManagementPageComponent;
  let fixture: ComponentFixture<ReasoningBranchManagementPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReasoningBranchManagementPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasoningBranchManagementPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
