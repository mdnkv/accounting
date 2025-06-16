import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {RoleService} from '../../../roles/services/role';

@Component({
  selector: 'app-dashboard-view',
  imports: [],
  templateUrl: './dashboard-view.html',
  styleUrl: './dashboard-view.css'
})
export class DashboardView implements OnInit{

  router: Router = inject(Router)
  roleService: RoleService = inject(RoleService)

  ngOnInit() {
    this.roleService.getActiveRole().subscribe({
      next: result => {
        console.log(result)
      },
      error: (err: HttpErrorResponse)=> {
        console.log(err)
        if (err.status == 404){
          // redirect to roles list view
          this.router.navigateByUrl('/organizations/create')
        }
      }
    })
  }

}
