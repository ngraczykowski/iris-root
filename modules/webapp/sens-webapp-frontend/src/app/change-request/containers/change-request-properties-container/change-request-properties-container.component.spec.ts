import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeRequestPropertiesContainerComponent } from './change-request-properties-container.component';

describe('ChangeRequestPropertiesContainerComponent', () => {
  let component: ChangeRequestPropertiesContainerComponent;
  let fixture: ComponentFixture<ChangeRequestPropertiesContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangeRequestPropertiesContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeRequestPropertiesContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
