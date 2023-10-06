package ru.phestrix.service;

import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Customer;
import ru.phestrix.storage.repositories.CustomerRepository;
import ru.phestrix.storage.repositories.GoodRepository;
import ru.phestrix.storage.repositories.PurchaseRepository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SearchService {
    private Connection connection = DatabaseConnection.getConnection();
    CustomerRepository customerRepository;
    GoodRepository goodRepository;
    PurchaseRepository purchaseRepository;

    public void search(String surname,
                       String goodName,
                       Integer countOfGood,
                       Integer minCost,
                       Integer maxCost,
                       Integer countOfBadCustomers) {
        searchBySurname(surname);
        searchGoodNameWithCountTimes(goodName, countOfGood);
        searchCustomersInInterval(minCost, maxCost);
        searchCustomersWithMinimumPurchases(countOfBadCustomers);
    }

    private void searchBySurname(String surname) {
        if (!surname.isEmpty()) {
            List<Customer> customerList = customerRepository.findBySurname(surname);
            if (!customerList.isEmpty()) {
                //TODO send list to json writer
            }
        }
    }

    private void searchGoodNameWithCountTimes(String goodName, Integer countOfGood) {
        if (!goodName.isEmpty() && countOfGood < Integer.MAX_VALUE && countOfGood > 0) {
            Integer goodId = goodRepository.findGoodIdByName(goodName);
            if (goodId == -1) {
                return;
            }
            List<Integer> customerList = purchaseRepository.
                    findCustomersIdWhoHasGoodIdCountTimes(goodId, countOfGood);

        }
    }

    private void searchCustomersInInterval(Integer minCost, Integer maxCost) {
        if (minCost < Integer.MAX_VALUE && maxCost > 0) {
            List<Integer> listOfCustomerIds = customerRepository.
                    findCustomersIdWithCostOfBuysInInterval(minCost, maxCost);
            if (listOfCustomerIds.isEmpty()) return;
            ArrayList<Customer> customers = new ArrayList<>();
            for (Integer id : listOfCustomerIds) {
                customers.add(customerRepository.findById(id));
            }
        }
    }

    private void searchCustomersWithMinimumPurchases(Integer countOfBadCustomers) {
        if (countOfBadCustomers == Integer.MAX_VALUE) return;
        ArrayList<Integer> listOfCustomerIds = customerRepository.
                findCustomerIdsWithMinQuantityOfPurchases(countOfBadCustomers);
        if (listOfCustomerIds.isEmpty()) return;
        ArrayList<Customer> customers = new ArrayList<>();
        for (Integer id : listOfCustomerIds) {
            customers.add(customerRepository.findById(id));
        }
    }
}
