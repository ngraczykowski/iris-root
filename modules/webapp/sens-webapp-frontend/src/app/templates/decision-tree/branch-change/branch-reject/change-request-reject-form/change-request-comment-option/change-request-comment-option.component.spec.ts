import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeRequestRejectFormModule } from '@app/templates/decision-tree/branch-change/branch-reject/change-request-reject-form/change-request-reject-form.module';
import { TestModule } from '@app/test/test.module';

import { ChangeRequestCommentOptionComponent } from './change-request-comment-option.component';

describe('ChangeRequestCommentOptionComponent', () => {
  let component: ChangeRequestCommentOptionComponent;
  let fixture: ComponentFixture<ChangeRequestCommentOptionComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            ChangeRequestRejectFormModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeRequestCommentOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
