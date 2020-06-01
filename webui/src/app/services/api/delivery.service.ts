import { Injectable, Inject                  } from '@angular/core';
import { Router                              } from '@angular/router';

import { Observable,Subject,BehaviorSubject  } from 'rxjs';
import { catchError                          } from 'rxjs/operators';
import { ApiRequestService                   } from '../auth/api-request.service';
import { Delivery                            } from '../../models/delivery';


export interface DeliveryResponse{
    success:boolean;
    message:string;
}

@Injectable()
export class DeliveryService {

    constructor(
        private router: Router,
        private apiRequest: ApiRequestService
    ) {}


    createDelivery(delivery: Delivery): Observable < any > {
        let me = this;

        let deliveryDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let deliveryResponse: DeliveryResponse;

        this.apiRequest.post('api/delivery', delivery)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    deliveryResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    deliveryResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                deliveryDataSubject.next(deliveryResponse);
            },
            err => {
                deliveryResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                deliveryDataSubject.next(deliveryResponse);
            });

        return deliveryDataSubject;
    }
    
    updateDelivery(delivery: Delivery): Observable < any > {
        let me = this;

        let deliveryDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let deliveryResponse: DeliveryResponse;

        this.apiRequest.put('api/delivery', delivery)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    deliveryResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    deliveryResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                deliveryDataSubject.next(deliveryResponse);
            },
            err => {
                deliveryResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                deliveryDataSubject.next(deliveryResponse);
            });

        return deliveryDataSubject;
    }
    
    deleteDelivery(deliveryId): Observable < any > {
        let me = this;

        let deliveryDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let deliveryResponse: DeliveryResponse;

        this.apiRequest.delete('api/delivery/' + deliveryId)
            .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    deliveryResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    deliveryResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                deliveryDataSubject.next(deliveryResponse);
            },
            err => {
                deliveryResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                deliveryDataSubject.next(deliveryResponse);
            });

        return deliveryDataSubject;
    }
}