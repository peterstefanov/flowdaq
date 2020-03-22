import { Component, OnInit, OnDestroy, TemplateRef, ViewChild, HostListener } from '@angular/core';
import { ColumnMode, SelectionType   } from '@swimlane/ngx-datatable';
import { Router, NavigationEnd       } from '@angular/router';
import { CustomerService             } from '../../services/api/customer.service';
import { UserInfoService             } from '../../services/user-info.service';

import { Facility                    } from '../../models/facility';
import { FacilityManagementService   } from '../../services/api/usermanagement/facilitymanagement.service';

@Component({
	selector: 'f-facilities-pg',
	templateUrl: './facilities.component.html',
    styleUrls: [ './facilities.scss'],
})

export class FacilitiesComponent implements OnDestroy {

   @ViewChild('myTable', {static: false}) table: any;

    rows: any[];
    isLoading: boolean = false;
    navigationSubscription;
    expanded: any = {};
    timeout: any;
    isToggled: boolean = false;
    selected = [];;
    public customerName: string = "";
    
    ColumnMode = ColumnMode;
    SelectionType = SelectionType;

    public facilityErrorMsg: string = '';
    public facilitySuccessMsg: string = '';
    
    editFacilityObject: Facility = {distributorId: 0, id: 0, relatedTo: null, userName: '', email: '', firstName: '', lastName: '', contact: '', altContact: '', enabled: true, phoneNumber: '', companyName: '', addressId: 0, addressLine1: '', addressLine2: '',addressLine3: '', city: '', state: '', country: '', postalCode: '', role: 'facility' } as Facility;
    facilityEditModal = false;
    
    constructor(private router: Router, private customerService: CustomerService, private userInfoService: UserInfoService, private facilityManagementService: FacilityManagementService) {
        this.navigationSubscription = this.router.events.subscribe((e: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (e instanceof NavigationEnd) {
                this.getPageData();
            }
        });
        this.customerName = this.userInfoService.getCustomerCompany();
    }

    getPageData() {
        let me = this;
        me.isLoading = true;
        this.customerService.getFacilities(this.userInfoService.getCustomerId()).subscribe((data) => {
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

    /**Facility action*/   
    /**Facility action*/
    editFacility(row) {
       this.editFacilityObject = {id: row.customerId, distributorId: row.userItem.distributorId, relatedTo: row.relatedTo, userName: row.userItem.userId, email: row.userItem.email.trim(), firstName: row.userItem.firstName, lastName: row.userItem.lastName, contact: row.contact, altContact: row.altContact, enabled: row.userItem.enabled, phoneNumber: row.userItem.phoneNumber, companyName: row.companyName, addressId: row.userItem.address.id, addressLine1: row.userItem.address.addressLine1, addressLine2: row.userItem.address.addressLine2, addressLine3: row.userItem.address.addressLine3, city: row.userItem.address.city, state: row.userItem.address.state, country: row.userItem.address.country, postalCode: row.userItem.address.postalCode, role: 'facility'} ;
       this.facilityEditModal = true;       
    }     
       
    enableFacility(row) {   
       this.editFacilityObject = {id: row.customerId, distributorId: row.userItem.distributorId, relatedTo: row.relatedTo, userName: row.userItem.userId, email: row.userItem.email.trim(), firstName: row.userItem.firstName, lastName: row.userItem.lastName, contact: row.contact, altContact: row.altContact, enabled: true, phoneNumber: row.userItem.phoneNumber, companyName: row.companyName, addressId: row.userItem.address.id, addressLine1: row.userItem.address.addressLine1, addressLine2: row.userItem.address.addressLine2, addressLine3: row.userItem.address.addressLine3, city: row.userItem.address.city, state: row.userItem.address.state, country: row.userItem.address.country, postalCode: row.userItem.address.postalCode, role: 'facility'} ;   
       this.saveFacility();
    } 
    
    disableFacility(row) {   
       this.editFacilityObject = {id: row.customerId, distributorId: row.userItem.distributorId, relatedTo: row.relatedTo, userName: row.userItem.userId, email: row.userItem.email.trim(), firstName: row.userItem.firstName, lastName: row.userItem.lastName, contact: row.contact, altContact: row.altContact, enabled: false, phoneNumber: row.userItem.phoneNumber, companyName: row.companyName, addressId: row.userItem.address.id, addressLine1: row.userItem.address.addressLine1, addressLine2: row.userItem.address.addressLine2, addressLine3: row.userItem.address.addressLine3, city: row.userItem.address.city, state: row.userItem.address.state, country: row.userItem.address.country, postalCode: row.userItem.address.postalCode, role: 'facility'} ;   
       this.saveFacility();
    } 
    
    createDelivery(row) {
       console.log('create delivery');     
       console.log(row);  
    }  
    
    /* Facility dialog */    
    public deleteFacility(row) {   
       this.facilitySuccessMsg = '';
       this.facilityErrorMsg = '';    
        
       this.facilityManagementService.deleteFacility(row.customerId)
           .subscribe(resp => {
               if (resp.success === false) {
                   this.facilityErrorMsg = resp.message;
                   return;
               } else if (resp.success === true) {
                   this.cancelFacility();
                   this.facilitySuccessMsg = resp.message;
                   /**On success unselect the user and reload the table*/
                   this.remove();
                   this.getPageData();
                   return;
               }              
           }
       );
    }  
    
    public saveFacility(): void {
        this.facilitySuccessMsg = '';
        this.facilityErrorMsg = '';
        
        this.facilityManagementService.updateFacility(this.editFacilityObject)
            .subscribe(resp => {
                if (resp.success === false) {
                    this.facilityErrorMsg = resp.message;
                    return;
                } else if (resp.success === true) {
                    this.cancelFacility();
                    this.facilitySuccessMsg = resp.message;
                    /**On success unselect the user and reload the table*/
                    this.remove();
                    this.getPageData();
                    return;
                 }              
             }
         );
    }

    public cancelFacility(): void {
        this.facilityEditModal = false;
    }
    
    public closeFacilitySuccess(): void {
        this.facilitySuccessMsg = '';
    }
    /* END Facility dialog */
    /** END Facility action*/
            
    toggleExpandRow(row) {
        console.log('Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
        this.isToggled = true;
        console.log('Detail Toggled', event);
    }

    onSelect({ selected }) {
        console.log('Select Event', selected, this.selected);
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