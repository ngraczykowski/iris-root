import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { PageableDynamicTableModule } from '../pageable-dynamic-table.module';
import { PageRowCountSelectorComponent } from './page-row-count-selector.component';

describe('PageRowCountSelectorComponent', () => {
  let component: PageRowCountSelectorComponent;
  let fixture: ComponentFixture<PageRowCountSelectorComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            PageableDynamicTableModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PageRowCountSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should shouldDislayRowCountPerPageSelector return true if there are 2 options', () => {
    component.rowCountPerPageOptions = [10, 20];

    expect(component.shouldDisplayRowCountPerPageSelector()).toBeTruthy();
  });

  it('should shouldDislayRowCountPerPageSelector return false if there are less than 2 options', () => {
    component.rowCountPerPageOptions = [10];

    expect(component.shouldDisplayRowCountPerPageSelector()).toBeFalsy();
  });

  it('should shouldDislayRowCountPerPageSelector return true if there is no options', () => {
    component.rowCountPerPageOptions = null;

    expect(component.shouldDisplayRowCountPerPageSelector()).toBeFalsy();
  });

  it('should emit valid event onOptionChange', () => {
    fixture.detectChanges();
    spyOn(component.rowCountPerPageChange, 'emit');

    component.onOptionChange(10);

    expect(component.rowCountPerPageChange.emit).toHaveBeenCalledTimes(1);
    expect(component.rowCountPerPageChange.emit).toHaveBeenCalledWith(10);
  });
});
