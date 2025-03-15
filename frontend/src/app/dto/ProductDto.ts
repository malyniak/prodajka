import {SafeUrl} from "@angular/platform-browser";

export interface ProductDto {
  id: string
  display: string | null;
  frequencies: string | null;
  camera: string | null;
  versionOS: string | null;
  batteryPower: string | null;
  otherFunctions: string | null;
  is3g: boolean | null;
  isLTE: boolean | null;
  is5g: boolean | null;
  isNanoSim: boolean | null;
  is_2Sim: boolean | null;
  productId: string | null
  fullName: string | null
  price: Number | null
  brand: string | null
  model: string | null
  avgGrade: Number | null
  saleStatus: string | null
  productCategory: string
  isAccess: boolean | null
  description: string | null
  color : string | null
  imageUrl: string | null | SafeUrl
  videoUrl: string | null
}
