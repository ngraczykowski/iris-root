interface DictionaryPatternFormatter {
  matches(value: string): boolean;
  format(value: string): string;
}

abstract class RegexDictionaryPatternFormatter {

  constructor(private pattern: RegExp) { }

  matches(value: string): boolean {
    return this.pattern.test(value);
  }
}

class UnderscoreNotationPatternFormatter extends RegexDictionaryPatternFormatter {

  constructor() {
    super(/^[A-Za-z0-9]+(_[A-Za-z0-9]+)+$/);
  }

  format(value: string): string {
    let shouldUpperCaseNextChar = true;
    const charArray = [];
    for (const char of value) {
      if (char === '_') {
        charArray.push(' ');
        shouldUpperCaseNextChar = true;
      } else {
        charArray.push(shouldUpperCaseNextChar ? char.toUpperCase() : char.toLowerCase());
        shouldUpperCaseNextChar = false;
      }
    }
    return charArray.join('');
  }
}

class JavaConventionPatternFormatter extends RegexDictionaryPatternFormatter {

  constructor() {
    super(/^[a-z]+(([A-Z][a-z]*))*$/);
  }

  format(value: string): string {
    const charArray = [];
    for (const char of value) {
      if (/^[A-Z]/.test(char)) {
        charArray.push(' ');
      }
      charArray.push(charArray.length === 0 ? char.toUpperCase() : char);
    }
    return charArray.join('');
  }
}

export class FriendlyTextFormatter {

  constructor(private formatters: DictionaryPatternFormatter[] = [
    new UnderscoreNotationPatternFormatter(),
    new JavaConventionPatternFormatter()
  ]) { }

  apply(value) {

    if (value && typeof value === 'string') {
      for (const formatter of this.formatters) {
        if (formatter.matches(value)) {
          return formatter.format(value);
        }
      }
    }

    return value;
  }
}
