package ru.phestrix.service;

import ru.phestrix.dto.CustomerDto;
import ru.phestrix.dtoProducer.CustomerDtoProducer;
import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Customer;
import ru.phestrix.storage.repositories.CustomerRepository;
import ru.phestrix.storage.repositories.ProductRepository;
import ru.phestrix.storage.repositories.PurchaseRepository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SearchService {
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final PurchaseRepository purchaseRepository = new PurchaseRepository();


    public ArrayList<CustomerDto> searchByLastname(String surname) {
        ArrayList<CustomerDto> customerDtos = new ArrayList<>();
        if (!surname.isEmpty()) {
            List<Customer> customerList = customerRepository.findByLastName(surname);
            if (customerList.isEmpty()) {
                return null;
            }
            for (Customer customer : customerList) {
                customerDtos.add(CustomerDtoProducer.makeCustomerDto(customer));
            }
        }
        return customerDtos;
    }

    public ArrayList<CustomerDto> searchProductNameWithCountTimes(String goodName, Integer countOfGood) {
        if (goodName.isEmpty() || countOfGood == Integer.MAX_VALUE || countOfGood < 0) return null;
        Integer goodId = productRepository.findProductIdByName(goodName);
        if (goodId == -1) {
            return null;
        }
        List<Integer> customerList = purchaseRepository.
                findCustomersIdWhoHasGoodIdCountTimes(goodId, countOfGood);
        ArrayList<CustomerDto> customerDtos = new ArrayList<>();
        for (Integer id : customerList) {
            customerDtos.add(
                    CustomerDtoProducer
                            .makeCustomerDto(customerRepository.findById(id)
                            )
            );
        }
        return customerDtos;
    }

    public ArrayList<CustomerDto> searchCustomersInInterval(Integer minCost, Integer maxCost) {
        if (minCost == Integer.MAX_VALUE || maxCost < 0) return null;
        List<Integer> listOfCustomerIds = customerRepository.
                findCustomersIdWithCostOfBuysInInterval(minCost, maxCost);
        if (listOfCustomerIds.isEmpty()) return null;
        ArrayList<CustomerDto> customers = new ArrayList<>();
        for (Integer id : listOfCustomerIds) {
            customers.add(CustomerDtoProducer
                    .makeCustomerDto(
                            customerRepository.findById(id)
                    )
            );
        }
        return customers;
    }

    public ArrayList<CustomerDto> searchCustomersWithMinimumPurchases(Integer countOfBadCustomers) {
        if (countOfBadCustomers == Integer.MAX_VALUE) return null;
        ArrayList<Integer> listOfCustomerIds = customerRepository.
                findCustomerIdsWithMinQuantityOfPurchases(countOfBadCustomers);
        if (listOfCustomerIds.isEmpty()) return null;
        ArrayList<CustomerDto> customers = new ArrayList<>();
        for (Integer id : listOfCustomerIds) {
            customers.add(CustomerDtoProducer
                    .makeCustomerDto(
                            customerRepository.findById(id)
                    )
            );
        }
        return customers;
    }
}
