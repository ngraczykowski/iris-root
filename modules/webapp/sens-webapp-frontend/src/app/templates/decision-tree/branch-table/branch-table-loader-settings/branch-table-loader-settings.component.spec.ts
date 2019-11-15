import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../test/test.module';
import { BranchPage } from '../branch-page';
import { BranchTableModule } from '../branch-table.module';
import { BranchTableLoaderSettingsComponent } from './branch-table-loader-settings.component';

describe('BranchTableLoaderConfigurerComponent', () => {
  let component: BranchTableLoaderSettingsComponent;
  let fixture: ComponentFixture<BranchTableLoaderSettingsComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchTableModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchTableLoaderSettingsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init loader on init', () => {
    spyOn(component.loaderChange, 'emit');

    fixture.detectChanges();

    expect(component.loaderChange.emit).toHaveBeenCalledTimes(1);
  });

  it('should set error on load error', () => {
    const error = 'error';
    component.onLoadError(error);

    expect(component.error).toEqual(error);
  });

  it('should reset error on load success', () => {
    component.error = 'error';
    component.onLoadSuccess(<BranchPage> {});

    expect(component.error).toEqual(null);
  });

  it('should load total on load success', () => {
    component.onLoadSuccess(<BranchPage> {total: 90});

    expect(component.total).toEqual(90);
  });

  it('should reset total on load error', () => {
    component.onLoadError('error');

    expect(component.total).toEqual(null);
  });
});
