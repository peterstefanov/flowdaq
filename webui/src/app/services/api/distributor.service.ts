import { Injectable, Inject } from '@angular/core';
import { Observable, ReplaySubject, Subject } from 'rxjs';
import { ApiRequestService } from '../auth/api-request.service';
import { HttpParams} from "@angular/common/http";


@Injectable()
export class DistributorService {

    constructor(
        private apiRequest: ApiRequestService
    ) {}


    getDistributors(): Observable < any > {
        let me = this;

        let distributorListSubject = new Subject < any > ();

        this.apiRequest.getNoParams('api/distributors')
        .subscribe(jsonResp => {
            let itemsDisplay = jsonResp.items.map(function(value, i, a) {
                let newRow = Object.assign({}, value, {
                    userId: value.userId,
                    email: value.email,
                    displayName: value.firstName + " " + value.lastName,
                    distributorName: value.distributorName,
                    distributorId: value.distributorId,
                });
                return newRow;
            });

            let returnObj = Object.assign({}, jsonResp, {
                items: itemsDisplay
            })
            distributorListSubject.next(returnObj);
        });

        return distributorListSubject;
    }
    
    getDistributorsDeliveries(distributorId): Observable < any > {
        let me = this;

        let distributorDeliveriesListSubject = new Subject < any > ();

        this.apiRequest.getNoParams('api/distributors/delivery/' + distributorId)
        .subscribe(jsonResp => {
            let itemsDisplay = jsonResp.items.map(function(value, i, a) {
                let newRow = Object.assign({}, value, {});
                return newRow;
            });

            let returnObj = Object.assign({}, jsonResp, {
                items: itemsDisplay
            })
            distributorDeliveriesListSubject.next(returnObj);
        });

        return distributorDeliveriesListSubject;
    }
}