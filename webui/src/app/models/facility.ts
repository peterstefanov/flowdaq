export interface Facility {
  distributorId: number;
  id: number;
  relatedTo: number;
  userName: string;
  email: string;
  firstName: string;
  lastName: string;
  contact: string;
  altContact: string;
  enabled: boolean;
  phoneNumber: string;
  companyName: string;
  addressId: number;
  addressLine1: string;
  addressLine2: string;
  addressLine3: string;
  city: string;
  state: string;
  postalCode: string;  
  country: string;
  role: 'facility';
}