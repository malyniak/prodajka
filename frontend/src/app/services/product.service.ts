import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ProductDto} from "../dto/ProductDto";
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseApiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient,
    private sanitizer: DomSanitizer
  ) {
  }
  getProducts(): Observable<ProductDto[]> {
   return  this.http.get<ProductDto[]>('http://localhost:8080/api/product')
  }


    getImage(imageKey: string): Observable<SafeUrl> {
      const url = `http://localhost:8080/api/product/download/${encodeURIComponent(imageKey)}`;
      return this.http.get(url, {responseType: 'blob'}).pipe(
        map(blob => {
          console.log(imageKey);
          console.log(url);
          const objectUrl = URL.createObjectURL(blob);
          return this.sanitizer.bypassSecurityTrustUrl(objectUrl);
        })
      );
    }
  
}
