import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'highlight'
})
export class HighlightSearchPipe implements PipeTransform {
  transform(value: any, args: any): any {
    if (!args || value === null) {
      return value;
    }
    const regexp = new RegExp(args, 'gi');
    return value.replace(regexp, '<b>' + args + '</b>');
  }
}
