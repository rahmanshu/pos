package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 12/04/2017.
 */

public class EndOfShiftModel implements Serializable {

    private static final long serialVersionUID = 1183786945429792573L;

    public double setoranAwal;
    public double cashIncome;
    public double cash;
    public double netSales;
    public double netReturn;
    public double ppn;
    public double credit;
    public double debit;
    public double discount;
    public int jumlahTransaksi;


    /*
    public EndOfShiftModel( double setoranAwal, double cashIncome, double cash, double netSales, double netReturn, double ppn, double credit, double debit, double discount){
        this.setoranAwal = setoranAwal;
        this.cashIncome = cashIncome;
        this.cash = cash;
        this.netSales = netSales;
        this.netReturn = netReturn;
        this.ppn = ppn;
        this.credit = credit;
        this.debit = debit;
        this.discount = discount;
    }
    */

    public int getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public void setJumlahTransaksi(int jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public double getSetoranAwal() {
        return setoranAwal;
    }

    public void setSetoranAwal(double setoranAwal) {
        this.setoranAwal = setoranAwal;
    }

    public double getCashIncome() {
        return cashIncome;
    }

    public void setCashIncome(double cashIncome) {
        this.cashIncome = cashIncome;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getNetSales() {
        return netSales;
    }

    public void setNetSales(double netSales) {
        this.netSales = netSales;
    }

    public double getNetReturn() {
        return netReturn;
    }

    public void setNetReturn(double netReturn) {
        this.netReturn = netReturn;
    }

    public double getPpn() {
        return ppn;
    }

    public void setPpn(double ppn) {
        this.ppn = ppn;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
