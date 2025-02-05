import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ProductDto} from "../dto/ProductDto";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) {
  }
  getProducts(): Observable<ProductDto[]> {
   return  this.http.get<ProductDto[]>('http://localhost:8080/api/product')
  }
}
