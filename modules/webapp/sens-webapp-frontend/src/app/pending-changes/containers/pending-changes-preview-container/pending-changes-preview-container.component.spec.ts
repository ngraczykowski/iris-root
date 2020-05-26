import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingChangesPreviewContainerComponent } from './pending-changes-preview-container.component';

describe('ChangeRequestPreviewContainerComponent', () => {
  let component: PendingChangesPreviewContainerComponent;
  let fixture: ComponentFixture<PendingChangesPreviewContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PendingChangesPreviewContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PendingChangesPreviewContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
