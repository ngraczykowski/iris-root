import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReasoningBranchManagementPageComponent } from './reasoning-branch-management-page.component';
import { TestModule } from '@app/test/test.module';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('ReasoningBranchManagementPageComponent', () => {
  let component: ReasoningBranchManagementPageComponent;
  let fixture: ComponentFixture<ReasoningBranchManagementPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReasoningBranchManagementPageComponent ],
      imports: [ TestModule ],
      schemas: [ NO_ERRORS_SCHEMA ]
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
