package ru.phestrix.controller;

import org.json.JSONObject;
import ru.phestrix.dto.CustomerDto;
import ru.phestrix.jsonParser.JSONParser;
import ru.phestrix.jsonParser.utility.CriteriaType;
import ru.phestrix.service.SearchService;
import ru.phestrix.service.StatService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final SearchService searchService = new SearchService();
    private final StatService statService = new StatService();

    public void execute(String[] args) {

        JSONParser jsonParser = new JSONParser(args[2]);

        List<JSONObject> jsonObjectList = jsonParser.getJsonFromFile(args[1]);
        if (jsonObjectList == null) {
            jsonParser.writeError("cannot open input file");
            jsonParser.closeWriter();
            return;
        }
        if (args[0].equals("stat")) {
            stat(jsonParser,
                    Date.valueOf(jsonObjectList.get(0).get("startDate").toString()),
                    Date.valueOf(jsonObjectList.get(0).get("endDate").toString()));
        }
        if (args[0].equals("search")) {
            search(jsonParser, jsonObjectList);
        }

        jsonParser.closeWriter();
    }

    private void stat(JSONParser jsonParser, Date startDate, Date endDate) {
        jsonParser.writeStatistic(
                statService.stat(startDate, endDate),
                statService.getTotalDays(),
                statService.getTotalExpenses(),
                statService.getAvgExpenses());
    }

    private void search(JSONParser jsonParser, List<JSONObject> listOfCriterias) {
        for (JSONObject obj : listOfCriterias) {
            CriteriaType criteriaType = jsonParser.parseCriteria(obj);
            if (criteriaType != CriteriaType.DATES) {
                jsonParser.writeCriteria(obj);
            }

            switch (criteriaType) {
                case LASTNAME:
                    ArrayList<CustomerDto> customersByLastName =
                            searchService.searchByLastname(
                                    obj.optString("lastName"));
                    jsonParser.writeDtoList(customersByLastName);
                    break;
                case BAD_CUSTOMERS:
                    ArrayList<CustomerDto> badCustomers =
                            searchService.searchCustomersWithMinimumPurchases(
                                    obj.optInt("badCustomers"));
                    jsonParser.writeDtoList(badCustomers);
                    break;
                case PRODUCT_COUNT:
                    ArrayList<CustomerDto> customersWithProduct =
                            searchService.searchProductNameWithCountTimes(
                                    obj.optString("productName"),
                                    obj.optInt("minTimes"));
                    jsonParser.writeDtoList(customersWithProduct);
                    break;
                case MIN_MAX_EXPENSES:
                    ArrayList<CustomerDto> customersInInterval =
                            searchService.searchCustomersInInterval(
                                    obj.optInt("minExpenses"),
                                    obj.optInt("maxExpenses")
                            );
                    jsonParser.writeDtoList(customersInInterval);
                    break;
            }
        }
        jsonParser.writeHeader("search");
        jsonParser.write("search");
    }
}
