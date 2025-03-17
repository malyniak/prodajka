import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserBasketDto } from '../dto/UserBasketDto';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class BasketService {

  constructor(private http: HttpClient) {}
  
  createBasket(): Observable<void> {
  return this.http.post<void>('http://localhost:8080/api/basket', {})
  }

  getBasket(): Observable<UserBasketDto> {
    return this.http.get<UserBasketDto>('http://localhost:8080/api/basket')
  }

  increaseCount(productId: string, count: number) {
    console.log(count)
    const httpParams = new HttpParams().set('productId', productId).set('count', count.toString())
    console.log(httpParams)
    return this.http.patch('http://localhost:8080/api/basket', {}, {params: httpParams})
  }
}
