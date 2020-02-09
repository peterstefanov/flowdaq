export interface Distributor {
  id: number;
  userName: string;
  email: string;
  firstName: string;
  lastName: string;
  enabled: boolean;
  companyName: string;
  addressId: number;
  addressLine1: string;
  addressLine2: string;
  addressLine3: string;
  city: string;
  state: string;
  postalCode: string;  
  country: string;
  role: 'distributor';
}