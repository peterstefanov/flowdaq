import { Component, OnInit, OnDestroy, TemplateRef, ViewChild, HostListener } from '@angular/core';
import { ColumnMode, SelectionType                                          } from '@swimlane/ngx-datatable';
import { Router, NavigationEnd                                              } from '@angular/router';
import { DistributorService                                                 } from '../../services/api/distributor.service';
import { UserInfoService                                                    } from '../../services/user-info.service';
import { Distributor                                                        } from '../../models/distributor';

@Component({
	selector: 'f-distributors-pg',
	templateUrl: './distributors.component.html',
    styleUrls: [ './distributors.scss'],
})

export class DistributorsComponent implements OnDestroy {

   @ViewChild('myTable', {
        static: false
    }) table: any;

    rows: any[];
    isLoading: boolean = false;
    navigationSubscription;
    expanded: any = {};
    timeout: any;
    isToggled: boolean = false;
    selected = [];
    public distributorName: string = "";
    
    ColumnMode = ColumnMode;
    SelectionType = SelectionType;

    editDistributorObject: Distributor = {id: 0, userName: '', email: '', firstName: '', lastName: '', companyName: '', addressLine1: '', addressLine2: '',addressLine3: '', city: '', state: '', country: '', postalCode: '', role: 'distributor' } as Distributor;
    distributorCreateModal = false;
    
    constructor(private router: Router, private distributorService: DistributorService, private userInfoService: UserInfoService) {
        this.navigationSubscription = this.router.events.subscribe((e: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (e instanceof NavigationEnd) {
                this.getPageData();
            }
        });
    }

    getPageData() {

        if (!this.isToggled) {
            let me = this;
            me.isLoading = true;
            this.distributorService.getDistributors().subscribe((data) => {
                me.rows = data.items;
                me.isLoading = false;
            });
        }
    }

    onPage(event) {
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
            console.log('paged!', event);
        }, 100);
    }

    /**Distributor action*/
    editDistributor(row) {
       console.log('edit Distributor');     
       console.log(row); 
       this.editDistributorObject = {id: row.distributorId, userName: row.userId, email: row.email, firstName: row.firstName, lastName: row.lastName, companyName: row.displayName, addressLine1: row.address.addressLine1, addressLine2: row.address.addressLine2, addressLine3: row.address.addressLine3, city: row.address.city, state: row.address.state, country: row.address.country, postalCode: row.address.postalCode, role: 'distributor'} ;
       this.distributorCreateModal = true;           
    }     
    
    deleteDistributor(row) {
       console.log('delete Distributor');     
       console.log(row);  
    }  
  
   /** END Distributor action*/
       
   /* Distributor dialog */
    public saveDistributor(): void {
        console.log('Save dialog distributor');
        console.log(this.editDistributorObject);
        //call service to update the Object
        this.cancelDistributor();
    }

    public cancelDistributor(): void {
        this.distributorCreateModal = false;
    }
    /* END Distributor dialog */
    
    toggleExpandRow(row) {
        console.log('Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
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