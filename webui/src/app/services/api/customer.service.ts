import { Injectable, Inject } from '@angular/core';
import { Observable, ReplaySubject, Subject } from 'rxjs';
import { ApiRequestService } from '../auth/api-request.service';
import { HttpParams} from "@angular/common/http";


@Injectable()
export class CustomerService {

    constructor(
        private apiRequest: ApiRequestService
    ) {}


    getCustomers(distributorId): Observable < any > {
        let me = this;

        let customerListSubject = new Subject < any > ();

        this.apiRequest.getNoParams('api/customers/' + distributorId)
        .subscribe(jsonResp => {
            let itemsDisplay = jsonResp.items.map(function(value, i, a) {
                let newRow = Object.assign({}, value, {
                    companyName: value.companyName,
                    full: value.full,
                    empty: value.empty,
                    max: value.max,
                    capacity: value.capacity,
                    deliveryDate: value.deliveryDate,
                    count: value.count
                });
                return newRow;
            });

            let returnObj = Object.assign({}, jsonResp, {
                items: itemsDisplay
            })
            customerListSubject.next(returnObj);
        });

        return customerListSubject;
    }
    
    getFacilities(customerId): Observable < any > {
        let me = this;

        let customerListSubject = new Subject < any > ();

        this.apiRequest.getNoParams('api/facilities/' + customerId)
        .subscribe(jsonResp => {
            let itemsDisplay = jsonResp.items.map(function(value, i, a) {
                let newRow = Object.assign({}, value, {
                    companyName: value.companyName,
                    full: value.full,
                    empty: value.empty,
                    max: value.max,
                    capacity: value.capacity,
                    deliveryDate: value.deliveryDate,
                    count: value.count
                });
                return newRow;
            });

            let returnObj = Object.assign({}, jsonResp, {
                items: itemsDisplay
            })
            customerListSubject.next(returnObj);
        });

        return customerListSubject;
    }
    
    getCustomerDeliveries(customerId): Observable < any > {
        let me = this;

        let customersDeliveriesListSubject = new Subject < any > ();

        this.apiRequest.getNoParams('api/customers/delivery/' + customerId)
        .subscribe(jsonResp => {
            let itemsDisplay = jsonResp.items.map(function(value, i, a) {
                let newRow = Object.assign({}, value, {});
                return newRow;
            });

            let returnObj = Object.assign({}, jsonResp, {
                items: itemsDisplay
            })
            customersDeliveriesListSubject.next(returnObj);
        });

        return customersDeliveriesListSubject;
    }
     
   getFacilityDeliveries(customerId): Observable < any > {
        let me = this;

        let facilitiesDeliveriesListSubject = new Subject < any > ();

        this.apiRequest.getNoParams('api/facilities/delivery/' + customerId)
        .subscribe(jsonResp => {
            let itemsDisplay = jsonResp.items.map(function(value, i, a) {
                let newRow = Object.assign({}, value, {});
                return newRow;
            });

            let returnObj = Object.assign({}, jsonResp, {
                items: itemsDisplay
            })
            facilitiesDeliveriesListSubject.next(returnObj);
        });

        return facilitiesDeliveriesListSubject;
    }
    
    /**remove this method*/
    getAllDeliveries(): Observable < any > {
        let me = this;

        let allDeliveriesListSubject = new Subject < any > ();

        this.apiRequest.getNoParams('api/customers/delivery')
        .subscribe(jsonResp => {
            let itemsDisplay = jsonResp.items.map(function(value, i, a) {
                let newRow = Object.assign({}, value, {});
                return newRow;
            });

            let returnObj = Object.assign({}, jsonResp, {
                items: itemsDisplay
            })
            allDeliveriesListSubject.next(returnObj);
        });

        return allDeliveriesListSubject;
    }
}