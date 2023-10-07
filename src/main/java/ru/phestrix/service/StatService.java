package ru.phestrix.service;

import ru.phestrix.dto.StatDto;
import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Customer;
import ru.phestrix.storage.repositories.CustomerRepository;
import ru.phestrix.storage.repositories.PurchaseRepository;

import java.sql.Connection;
import java.sql.Date;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class StatService {
    PurchaseRepository purchaseRepository = new PurchaseRepository();
    CustomerRepository customerRepository = new CustomerRepository();
    private Double avgExpenses = 0.0;
    private Integer totalExpenses = 0;
    private Integer totalDays = 0;

    public ArrayList<StatDto> stat(Date startDate, Date endDate) {
        ArrayList<StatDto> statDtos = purchaseRepository.getStatistic(startDate, endDate);
        for (StatDto dto : statDtos) {
            totalExpenses = getTotalExpensesByCustomer(
                    customerRepository.findByFullName(dto.getFullName())
            );
        }
        avgExpenses = getAverageExpenses();
        totalDays = Math.abs(Period.between(startDate.toLocalDate(), endDate.toLocalDate()).getDays()) + 1;
        return sortByCustomer(statDtos);
    }

    private Integer getTotalExpensesByCustomer(Customer customer) {
        purchaseRepository.findTotalExpensesByCustomer(customer.getId());
        return purchaseRepository.findTotalExpensesByCustomer(customer.getId());
    }

    private Double getAverageExpenses() {
        return purchaseRepository.findAverageExpenses();
    }

    private ArrayList<StatDto> sortByCustomer(ArrayList<StatDto> statDtos) {
        return (ArrayList<StatDto>) statDtos
                .stream()
                .sorted(
                        (Comparator.comparing(StatDto::getExpenses)))
                .collect(Collectors.toList());
    }

    public Integer getTotalExpenses() {
        return totalExpenses;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public Double getAvgExpenses() {
        return avgExpenses;
    }
}