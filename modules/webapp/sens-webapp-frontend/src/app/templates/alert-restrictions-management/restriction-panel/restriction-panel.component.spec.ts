import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RestrictionPanelModule } from '@app/templates/alert-restrictions-management/restriction-panel/restriction-panel.module';
import { TestModule } from '@app/test/test.module';

import { RestrictionPanelComponent } from './restriction-panel.component';

describe('NewRestrictionPanelComponent', () => {
  let component: RestrictionPanelComponent;
  let fixture: ComponentFixture<RestrictionPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        TestModule,
        RestrictionPanelModule
      ]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RestrictionPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
