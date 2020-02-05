export interface Distributor {
  id: number;
  userName: string;
  email: string;
  firstName: string;
  lastName: string;
  companyName: string;
  addressLine1: string;
  addressLine2: string;
  addressLine3: string;
  city: string;
  state: string;
  postalCode: string;  
  role: 'distributor';
}