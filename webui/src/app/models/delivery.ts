export interface Delivery {   
  id: number;
  status: string;
  fromDistributorId: number;
  fromCustomerId: number;
  fromFacilityId: number;
  toCustomerId: number;
  toFacilityId: number;
  toCoolerId: number;
  driverId: number;
  vehicleId: number;
  deliveryDate: Date;
  actualDeliveryDate: Date;
  fullBottles: number;
  actualFullsDelivered: number;
  routeId: number;
  emptiesRetrieved: number;
  actualEmptiesRetrieved: number;
  deliveryNotes: string;
}