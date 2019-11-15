export class RequestMatcher {

  private readonly urlRegex: RegExp;
  private readonly httpMethods: Set<string>;

  public constructor(urlRegex: RegExp, methods: Array<string>) {
    this.urlRegex = urlRegex;
    this.httpMethods = new Set<string>(methods);
  }

  public matches(url: string, method: string): boolean {
    return this.httpMethods.has(method) && this.urlRegex.test(url);
  }
}
