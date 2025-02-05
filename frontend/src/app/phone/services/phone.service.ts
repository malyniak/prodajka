import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {ProductDto} from "../../dto/ProductDto";
import {PhoneDto} from "../../dto/PhoneDto";
import {catchError, map} from "rxjs/operators";
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class PhoneService {

  private baseApiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient, private sanitizer: DomSanitizer) {
  }

  getPhoneImage(imageKey: string): Observable<SafeUrl> {
    const url = `${this.baseApiUrl}/product/download/${encodeURIComponent(imageKey)}`;
    return this.http.get(url, {responseType: 'blob'}).pipe(
      map(blob => {
        console.log(imageKey);
        console.log(url);
        const objectUrl = URL.createObjectURL(blob);
        return this.sanitizer.bypassSecurityTrustUrl(objectUrl);
      })
    );
  }

  getPhones(): Observable<PhoneDto[]> {
    return this.http.get<PhoneDto[]>(`${this.baseApiUrl}/phone`);
  }

}
