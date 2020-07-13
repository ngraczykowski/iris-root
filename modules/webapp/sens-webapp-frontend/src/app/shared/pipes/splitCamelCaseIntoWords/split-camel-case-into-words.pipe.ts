import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'splitCamelCaseIntoWords'
})
export class SplitCamelCaseIntoWordsPipe implements PipeTransform {

  transform(value: string): string {
    return value ? value.replace(/([a-z])([A-Z])/g, '$1 $2') : value;
  }

}
