export interface Device {
  id: number;
  coolerIdentifier: number;
  deviceId: number;
  reorderThreshold: number;
  maxBottleCount: number;
  currentBottleCount: number;
  customerId: number;
  deliveryLocationId: number;
  longitude: number;
  latitude: number;
  deviceName: string;
  lastDeliveryDate: string; 
  prevBottleCount: number;
  currentFull: number;
  currentEmpty: number;    
}