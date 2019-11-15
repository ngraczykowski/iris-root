import { Pipe, PipeTransform } from '@angular/core';
import { FriendlyTextFormatter } from '../../formatters/friendly/friendly-text-formatter';

@Pipe({
  name: 'friendly'
})
export class FriendlyPipe implements PipeTransform {

  private FriendlyTextFormatter = new FriendlyTextFormatter();

  transform(value: string): string {
    return this.FriendlyTextFormatter.apply(value);
  }
}
