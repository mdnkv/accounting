import {Component, input, output} from '@angular/core';
import {CurrencyPipe} from '@angular/common';
import {TransactionLine} from '../../models/transactions.models';

@Component({
  selector: 'app-transaction-line-item',
  imports: [CurrencyPipe],
  templateUrl: './transaction-line-item.html',
  styleUrl: './transaction-line-item.css'
})
export class TransactionLineItem {

  deleteTransactionLine = output()
  item = input.required<TransactionLine>()

  onDelete() {
    if (confirm('Do you want to remove this transaction line?')){
      this.deleteTransactionLine.emit()
    }
  }

}
