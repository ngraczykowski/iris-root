import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterMultipleValues'
})
export class FilterMultipleValuesPipe implements PipeTransform {

  transform(items: any, filter: any, isAnd: boolean): any {
    if (!filter) {
      return items;
    }

    if (!Array.isArray(items)) {
      return items;
    }

    const filterKeys = Object.keys(filter);

    if (isAnd) {
      return items.filter(item =>
          filterKeys.reduce((memo, keyName) =>
              (memo && new RegExp(filter[keyName], 'gi').test(item[keyName])) || filter[keyName] === '', true));
    } else {
      return items.filter(item => {
        return filterKeys.some((keyName) => {
          return this.containsValue(filter, keyName, item);
        });
      });
    }
  }

  private containsValue(filter: any, keyName, item): boolean {
    return new RegExp(filter[keyName], 'gi').test(item[keyName]) || filter[keyName] === '';
  }
}
