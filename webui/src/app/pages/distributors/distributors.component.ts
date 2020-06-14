import { Component, OnInit, OnDestroy, TemplateRef, ViewChild, HostListener } from '@angular/core';
import { ColumnMode, SelectionType                                          } from '@swimlane/ngx-datatable';
import { Router, NavigationEnd                                              } from '@angular/router';
import { DistributorService                                                 } from '../../services/api/distributor.service';
import { UserInfoService                                                    } from '../../services/user-info.service';
import { Distributor                                                        } from '../../models/distributor';
import { DistributorManagementService                                       } from '../../services/api/usermanagement/distributormanagement.service';


import '@clr/icons/shapes/core-shapes';
import '@clr/icons/shapes/essential-shapes';
import '@clr/icons/shapes/technology-shapes';

@Component({
	selector: 'f-distributors-pg',
	templateUrl: './distributors.component.html',
    styleUrls: [ './distributors.scss'],
})

export class DistributorsComponent implements OnDestroy {

    @ViewChild('myTable', {static: false}) table: any;

    rows: any[];
    isLoading: boolean = false;
    navigationSubscription;
    expanded: any = {};
    timeout: any;
    isToggled: boolean = false;
    selected = [];
    public distributorName: string = "";
    public overflowToggle: string = "close";
    
    ColumnMode = ColumnMode;
    SelectionType = SelectionType;

    public distributorErrorMsg: string = '';
    public distributorSuccessMsg: string = '';  
    
    editDistributorObject: Distributor = {id: 0, userName: '', email: '', firstName: '', lastName: '', enabled: true, phoneNumber: '', companyName: '', addressId: 0, addressLine1: '', addressLine2: '',addressLine3: '', city: '', state: '', country: '', postalCode: '', role: 'distributor' } as Distributor;
    distributorEditModal = false;
        
    constructor(
        private router: Router, 
        private distributorService: DistributorService, 
        private userInfoService: UserInfoService,
        private distributorManagementService: DistributorManagementService
        ) {
        this.navigationSubscription = this.router.events.subscribe((e: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (e instanceof NavigationEnd) {
                this.getPageData();
            }
        });
    }

    public getPageData() {
        let me = this;
        me.isLoading = true;
        this.distributorService.getDistributors().subscribe((data) => {
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

    /**Distributor action*/
    editDistributor(row) {
       this.editDistributorObject = {id: row.distributorId, userName: row.userId, email: row.email.trim(), firstName: row.firstName, lastName: row.lastName, enabled: row.enabled, phoneNumber: row.phoneNumber, companyName: row.distributorName, addressId: row.address.id, addressLine1: row.address.addressLine1, addressLine2: row.address.addressLine2, addressLine3: row.address.addressLine3, city: row.address.city, state: row.address.state, country: row.address.country, postalCode: row.address.postalCode, role: 'distributor'} ;
       this.distributorEditModal = true;           
    }     
    
    enableDistributor(row) {
       this.editDistributorObject = {id: row.distributorId, userName: row.userId, email: row.email.trim(), firstName: row.firstName, lastName: row.lastName, enabled: true, phoneNumber: row.phoneNumber,companyName: row.distributorName, addressId: row.address.id, addressLine1: row.address.addressLine1, addressLine2: row.address.addressLine2, addressLine3: row.address.addressLine3, city: row.address.city, state: row.address.state, country: row.address.country, postalCode: row.address.postalCode, role: 'distributor'} ;
       this.saveDistributor();          
    } 
    
    disableDistributor(row) {
       this.editDistributorObject = {id: row.distributorId, userName: row.userId, email: row.email.trim(), firstName: row.firstName, lastName: row.lastName, enabled: false, phoneNumber: row.phoneNumber,companyName: row.distributorName, addressId: row.address.id, addressLine1: row.address.addressLine1, addressLine2: row.address.addressLine2, addressLine3: row.address.addressLine3, city: row.address.city, state: row.address.state, country: row.address.country, postalCode: row.address.postalCode, role: 'distributor'} ;
       this.saveDistributor();          
    } 
    
    /* Distributor dialogs */
    public deleteDistributor(row) {       
       this.distributorManagementService.deleteDistributor(row.distributorId)
           .subscribe(resp => {
               if (resp.success === false) {
                   this.distributorErrorMsg = resp.message;
                   return;
               } else if (resp.success === true) {
                   this.cancelDistributor();
                   this.distributorSuccessMsg = resp.message;
                   /**On success unselect the user and reload the table*/
                   this.remove();
                   this.getPageData();
                   return;
               }              
           }
       );
    }  
 
    public saveDistributor(): void {
        
        this.distributorManagementService.updateDistributor(this.editDistributorObject)
            .subscribe(resp => {
                if (resp.success === false) {
                    this.distributorErrorMsg = resp.message;
                    return;
                } else if (resp.success === true) {
                    this.cancelDistributor();
                    this.distributorSuccessMsg = resp.message;
                    /**On success unselect the user and reload the table*/
                    this.remove();
                    this.getPageData();
                    return;
                 }              
             }
         );
    }

    public cancelDistributor(): void {
        this.distributorEditModal = false;
    }
    
    public closeDistributorSuccess(): void {
        this.distributorSuccessMsg = '';
    }
    /* END Distributor dialogs */   
    /** END Distributor action*/ 
    
    toggleExpandRow(row) {
        this.table.rowDetail.toggleExpandRow(row);
    }

    onOverflowToggle(overflowToggle) {
        console.log(overflowToggle);
        if (overflowToggle == "open") {
            this.overflowToggle = "close";
        } else {
            this.overflowToggle = "open";
        }
        
    }
    
    onDetailToggle(event) {
        this.isToggled = true;
    }

    onSelect({ selected }) {     
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
        this.userInfoService.setDistributorId(this.selected[0].distributorId, this.selected[0].distributorName);
    }

    remove() {
        this.selected = [];
    }
    
    onActivate(event) {
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