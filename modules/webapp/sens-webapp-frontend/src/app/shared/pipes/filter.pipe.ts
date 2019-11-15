import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
  transform(values: any[], query: string, field: string): any {
    if (query) {
      return this.reduceValues(values, query, field);
    }
    return values;
  }

  private reduceValues(values: any[], query: string, field: string) {
    return values.reduce((acc, next) => this.accumulateNext(acc, next, field, query), []);
  }

  private accumulateNext(acc: any[], next, field: string, query: string) {
    if (this.shouldPass(next, field, query)) {
      acc.push(next);
    }
    return acc;
  }

  private shouldPass(next, field: string, query: string) {
    return next && next[field] && this.normalize(next[field]).includes(this.normalize(query));
  }

  private normalize(value: string): string {
    return value.toLowerCase().trim().replace(/[^a-z0-9]/g, '');
  }
}
