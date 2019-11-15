import { environment } from '@env/environment';

export class PathExtractor {

  private readonly serverApiUrl: string = environment.serverApiUrl;

  public removeApiUrl(url: string) {
    const serverApiIndex = url.indexOf(this.serverApiUrl);
    if (serverApiIndex === 0) {
      return url.substring(this.serverApiUrl.length);
    } else {
      return url;
    }
  }
}
