import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';
import { FrontendSettings } from '../../../../../shared/frontend-settings/frontend-settings.model';
import { FrontendSettingsService } from '../../../../../shared/frontend-settings/frontend-settings.service';
import { TestModule } from '../../../../../test/test.module';
import { BranchTableModule } from '../../branch-table.module';

import { BranchLimitInfoComponent } from './branch-limit-info.component';

describe('BranchLimitInfoComponent', () => {
  let component: BranchLimitInfoComponent;
  let fixture: ComponentFixture<BranchLimitInfoComponent>;

  let settingsService: FrontendSettingsService;
  let translateService: TranslateService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchTableModule
          ]
        })
        .compileComponents();

    settingsService = TestBed.get(FrontendSettingsService);
    translateService = TestBed.get(TranslateService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchLimitInfoComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show return false before load settings', () => {
    component.total = 120;

    expect(component.show).toBeFalsy();
  });

  it('should show return false when total is undefined', () => {
    component.total = undefined;

    expect(component.show).toBeFalsy();
  });
});
