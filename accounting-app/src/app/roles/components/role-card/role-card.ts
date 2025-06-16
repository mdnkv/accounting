import {Component, input, output} from '@angular/core';
import {Role} from '../../models/roles.models';

@Component({
  selector: 'app-role-card',
  imports: [],
  templateUrl: './role-card.html',
  styleUrl: './role-card.css'
})
export class RoleCard {

  role = input.required<Role>()
  setActive = output<string>()

  onSetActive(){
    this.setActive.emit(this.role().id)
  }

}
