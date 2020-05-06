import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmChangesComponent } from './confirm-changes.component';
import { SharedModule } from '@app/shared/shared.module';
import { TestModule } from '@app/test/test.module';

describe('ConfirmChangesComponent', () => {
  let component: ConfirmChangesComponent;
  let fixture: ComponentFixture<ConfirmChangesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmChangesComponent ],
      imports: [ TestModule ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmChangesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
