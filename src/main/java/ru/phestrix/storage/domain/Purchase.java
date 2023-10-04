package ru.phestrix.storage.domain;

import java.time.LocalDate;
import java.util.List;

public class Purchase {
    private Long id;
    private LocalDate dateOfPurchase;
    private Customer customer;
    private List<Good> purchasedGoods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Good> getPurchasedGoods() {
        return purchasedGoods;
    }

    public void setPurchasedGoods(List<Good> purchasedGoods) {
        this.purchasedGoods = purchasedGoods;
    }
}
