import {Component, input, output} from '@angular/core';
import {CurrencyPipe} from '@angular/common';

import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';

import {TransactionLine} from '../../models/transactions.models';

@Component({
  selector: 'app-transaction-line-card',
  imports: [MatButtonModule, MatIconModule, CurrencyPipe],
  templateUrl: './transaction-line-card.html',
  styleUrl: './transaction-line-card.css'
})
export class TransactionLineCard {

  line = input.required<TransactionLine>()
  currency = input.required<string>()
  deleteLine = output()

  onDelete(){
    this.deleteLine.emit()
  }

}
