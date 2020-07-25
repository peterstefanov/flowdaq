import { Component, OnInit, OnDestroy, TemplateRef, ViewChild, HostListener } from '@angular/core';
import { ColumnMode, SelectionType   } from '@swimlane/ngx-datatable';
import { Router, NavigationEnd       } from '@angular/router';
import { DeviceService             } from '../../services/api/device.service';
import { UserInfoService             } from '../../services/user-info.service';

import { Device                    } from '../../models/device';

import { Delivery                    } from '../../models/delivery';
import { DeliveryService             } from '../../services/api/delivery.service';

@Component({
	selector: 'f-devices-pg',
	templateUrl: './devices.component.html',
    styleUrls: [ './devices.scss'],
})

export class DevicesComponent implements OnDestroy {

   @ViewChild('myTable', {static: false}) table: any;

    rows: any[];
    isLoading: boolean = false;
    navigationSubscription;
    expanded: any = {};
    timeout: any;
    isToggled: boolean = false;
    selected = [];;
    public customerName: string = "";
    userRole: string = "";
    
    ColumnMode = ColumnMode;
    SelectionType = SelectionType;
           
    public deliveryErrorMsg: string = '';
    public deliverySuccessMsg: string = '';
    
    createDeliveryObject: Delivery = {id: 0, status: '', fromDistributorId: 0, fromCustomerId: 0, fromFacilityId: 0, toCustomerId: 0, toFacilityId: 0, toCoolerId: 0, driverId: 0, vehicleId: 0, deliveryDate: new Date(), actualDeliveryDate: new Date(), fullBottles: 0, actualFullsDelivered: 0, routeId: 0, emptiesRetrieved: 0, actualEmptiesRetrieved: 0, deliveryNotes: ''} as Delivery;
    deliveryCreateModal = false;
    
    constructor(private router: Router, private deviceService: DeviceService, private userInfoService: UserInfoService, private deliveryService: DeliveryService) {
        this.navigationSubscription = this.router.events.subscribe((e: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (e instanceof NavigationEnd) {
                this.getPageData();
            }
        });
        this.customerName = this.userInfoService.getCustomerCompany();
        this.userRole = this.userInfoService.getUserRole();
    }
    
    getPageData() {

        let me = this;
        me.isLoading = true;
        this.deviceService.getDevices(this.userInfoService.getCustomerId()).subscribe((data) => {
            me.rows = data.items;
            me.isLoading = false;
        });
    }

    onPage(event) {
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
            console.log('paged!', event);
        }, 100);
    }
        
    /**Device action*/
    editDevice(row) {
       console.log('Edit device');     
       console.log(row);       
    }     
    /**END Device action*/
    
   /* Delivery action */       
    createDelivery(row): void {
        /**TODO Based on the role defined to and from. Same for the dialog */
        this.createDeliveryObject = {id: 0, status: 'SCHEDULED', fromDistributorId: 0, fromCustomerId: this.userInfoService.getCustomerId(), fromFacilityId: 0, toCustomerId: row.customerId, toFacilityId: 0, toCoolerId: 0, driverId: 0, vehicleId: 0, deliveryDate: new Date(), actualDeliveryDate: new Date(), fullBottles: 0, actualFullsDelivered: 0, routeId: 0, emptiesRetrieved: 0, actualEmptiesRetrieved: 0, deliveryNotes: ''};
        this.deliveryCreateModal = true;
    }
     
   /* Delivery dialog */      
    public saveDelivery(): void {
        this.deliverySuccessMsg = '';
        this.deliveryErrorMsg = '';

        var deliveryDate = new Date(this.createDeliveryObject.deliveryDate);
        var deliveryDateFormatted = new Date(deliveryDate.getTime() - (deliveryDate.getTimezoneOffset() * 60000 )).toISOString().split("T")[0];
        this.createDeliveryObject.deliveryDate = new Date(deliveryDateFormatted);
        /**set actual delivery date as the intended delivery date*/
        this.createDeliveryObject.actualDeliveryDate = new Date(deliveryDateFormatted);
   
        this.deliveryService.createDelivery(this.createDeliveryObject)
            .subscribe(resp => {
                if (resp.success === false) {
                    this.deliveryErrorMsg = resp.message;
                    return;
                } else if (resp.success === true) {
                    this.cancelDelivery();
                    this.deliverySuccessMsg = resp.message;
                    /**On success unselect the user and reload the table*/
                    this.remove();
                    this.getPageData();
                    return;
                 }              
             }
         );
    }

    public cancelDelivery(): void {
        this.deliveryCreateModal = false;
    }
    
    public closeDeliverySuccess(): void {
        this.deliverySuccessMsg = '';
    }
    /* END Delivery dialog */
    /* END Delivery action */
    
    toggleExpandRow(row) {
        console.log('Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
        this.isToggled = true;
        console.log('Detail Toggled', event);
    }

    onSelect({ selected }) {
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
    }

    remove() {
        this.selected = [];
    }
    
    onActivate(event) {
       console.log('Activate Event', event);
       if(event.type === 'click') {
           this.table.rowDetail.toggleExpandRow(event.row);
       }    
    }
    
    ngOnDestroy() {
        if (this.navigationSubscription) {
            this.navigationSubscription.unsubscribe();
        }
    }
}