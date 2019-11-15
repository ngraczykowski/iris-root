import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../test/test.module';
import { PageableDynamicTableModule } from '../pageable-dynamic-table.module';
import { PageSelectorComponent } from './page-selector.component';

describe('PageSelectorComponent', () => {
  let component: PageSelectorComponent;
  let fixture: ComponentFixture<PageSelectorComponent>;

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
    fixture = TestBed.createComponent(PageSelectorComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should refresh view on init', () => {
    component.rowCountPerPage = 10;
    component.total = 31;
    component.page = 2;

    fixture.detectChanges();

    expect(component.visibleRowsText).toEqual('11 - 20');
    expect(component.lastPage).toEqual(4);
  });

  it('should calculate lastPage correctly', () => {
    fixture.detectChanges();

    component.rowCountPerPage = 10;
    component.total = 0;
    expect(component.lastPage).toEqual(0);

    component.total = 21;
    expect(component.lastPage).toEqual(3);

    component.total = 130;
    expect(component.lastPage).toEqual(13);

    component.rowCountPerPage = 20;
    expect(component.lastPage).toEqual(7);
  });


  it('should update visibleRowsText correctly', () => {
    fixture.detectChanges();

    component.total = 23;
    component.page = 1;
    component.rowCountPerPage = 2;
    expect(component.visibleRowsText).toEqual('1 - 2');

    component.total = 10;
    component.page = 2;
    component.rowCountPerPage = 4;
    expect(component.visibleRowsText).toEqual('5 - 8');

    component.total = 200;
    component.page = 1;
    component.rowCountPerPage = 100;
    expect(component.visibleRowsText).toEqual('1 - 100');
  });

  it('should emit valid event onSelectPreviousPage', () => {
    component.rowCountPerPage = 5;
    component.page = 5;
    component.total = 50;
    fixture.detectChanges();
    spyOn(component.pageChange, 'emit');

    component.onSelectPreviousPage();

    expect(component.pageChange.emit).toHaveBeenCalledTimes(1);
    expect(component.pageChange.emit).toHaveBeenCalledWith(4);
  });

  it('should emit valid event onSelectNextPage', () => {
    component.rowCountPerPage = 5;
    component.page = 5;
    component.total = 50;
    fixture.detectChanges();
    spyOn(component.pageChange, 'emit');

    component.onSelectNextPage();

    expect(component.pageChange.emit).toHaveBeenCalledTimes(1);
    expect(component.pageChange.emit).toHaveBeenCalledWith(6);
  });
});
