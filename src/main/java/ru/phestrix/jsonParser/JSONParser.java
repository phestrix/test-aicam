package ru.phestrix.jsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.phestrix.dto.CustomerDto;
import ru.phestrix.dto.OrderedStatDto;
import ru.phestrix.jsonParser.utility.CriteriaType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static ru.phestrix.jsonParser.utility.CriteriaType.*;

public class JSONParser {

    private FileWriter outputWriter;
    private final JSONObject finalObject = new JSONObject();
    private final JSONArray results = new JSONArray();

    public JSONParser(String output) {
        try {

            outputWriter = new FileWriter(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<JSONObject> getJsonFromFile(String inputPath) {
        try {
            String strToParse = new String(Files
                    .readAllBytes(
                            Paths.get(inputPath)), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(strToParse);
            Map<String, Object> map = jsonObject.toMap();
            Set<String> set = map.keySet();
            if (set.size() == 1) {
                return getCriteriasList(jsonObject);
            }
            if (set.size() == 2) {
                return getDates(jsonObject);
            } else {
                writeError("unsupported format of json");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeError(String message) {
        JSONObject out = new JSONObject();
        out.put("type", "error");
        out.put("message", message);
        try {
            outputWriter.write(out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<JSONObject> getCriteriasList(JSONObject jsonObject) {
        LinkedList<JSONObject> criteriasList = new LinkedList<>();
        String criteriasString = jsonObject.get("criterias").toString();
        criteriasString = criteriasString.substring(1, criteriasString.length() - 1);
        String[] arrayOfCriterias = criteriasString.split("},");
        for (int i = 0; i < arrayOfCriterias.length - 1; i++) {
            criteriasList.add(new JSONObject(arrayOfCriterias[i].concat("}")));
        }
        criteriasList.add(new JSONObject(arrayOfCriterias[arrayOfCriterias.length - 1]));
        return criteriasList;
    }

    private List<JSONObject> getDates(JSONObject jsonObject) {
        LinkedList<JSONObject> statList = new LinkedList<>();
        statList.add(jsonObject);
        return statList;
    }

    public CriteriaType parseCriteria(JSONObject object) {
        Map<String, Object> map = object.toMap();
        Set<String> set = map.keySet();
        if (set.size() == 1) {
            return parseSingleParameterCriteria(set);
        }
        if (set.size() == 2) {
            return parseDoubleParameterCriteria(set);
        } else {
            return UNSUPPORTED_CRITERIA;
        }
    }

    private CriteriaType parseSingleParameterCriteria(Set<String> set) {
        if (set.contains("lastName")) {
            return LASTNAME;
        }
        if (set.contains("badCustomers")) {
            return BAD_CUSTOMERS;
        } else {
            return UNSUPPORTED_SINGLE_TYPE;
        }
    }

    private CriteriaType parseDoubleParameterCriteria(Set<String> set) {
        if (set.contains("productName") && set.contains("minTimes"))
            return PRODUCT_COUNT;
        else if (set.contains("minExpenses") && set.contains("maxExpenses"))
            return MIN_MAX_EXPENSES;
        else if (set.contains("startDate") && set.contains("endDate"))
            return DATES;
        return UNSUPPORTED_DOUBLE_TYPE;
    }

    public void writeHeader(String head) {
        finalObject.put("type", head);
    }

    public void writeCriteria(JSONObject object) {
        JSONObject criteria = new JSONObject();
        criteria.put("criteria", object);
        results.put(criteria);
    }

    public void writeDtoList(ArrayList<CustomerDto> list) {
        JSONObject result = new JSONObject();
        if (list == null) {
            result.put("results", "Ничего не нашлось");
            results.put(result);
            return;
        }
        JSONArray array = new JSONArray();
        for (CustomerDto customerDto : list) {
            JSONObject object = new JSONObject();
            object.put("lastName", customerDto.getLastname());
            object.put("firstName", customerDto.getName());
            array.put(object);
        }
        result.put("results", array);
        results.put(result);
    }

    public void write(String type) {
        try {
            if (type.equals("search"))
                finalObject.put("results", results);
            else finalObject.put("customers", results);
            outputWriter.write(finalObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeWriter() {
        try {
            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //using this for stat because of sorting in json object that placing name after purchase
    public void writeStatistic(
            ArrayList<OrderedStatDto> list,
            Integer totalDays,
            Integer totalExpenses,
            Double avgExpenses) {
        String str = "{\n"
                + "\t\"type\": \"stat\""
                + "\t\"totalDays\": " + totalDays +
                "\t\"customers\":" + list.toString()
                + ",\n\t\"totalExpenses\": " + totalExpenses + ",\n" +
                "\t\"averageExpenses\": " + avgExpenses + "\n}";
        try {
            outputWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
