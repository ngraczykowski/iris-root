import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeRequestPreviewContainerComponent } from './change-request-preview-container.component';

describe('ChangeRequestPreviewContainerComponent', () => {
  let component: ChangeRequestPreviewContainerComponent;
  let fixture: ComponentFixture<ChangeRequestPreviewContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangeRequestPreviewContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeRequestPreviewContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
