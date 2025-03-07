export interface RegisterRequest {
  email: string
  password: string
  confirmPassword: string
  firstname: string
  lastname: string | null
  phoneNumber: string
}
