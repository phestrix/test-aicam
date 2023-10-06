package ru.phestrix.jsonParser;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONParser {
    private final String inputPath;
    private final String outputPath;
    private FileWriter fileWriter;

    public JSONParser(String input, String output) {
        inputPath = input;
        outputPath = output;
    }

    public List<JSONObject> getJsonFromFile() {
        try {
            String strToParse = new String(Files.readAllBytes(Paths.get(inputPath)));
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
            fileWriter = new FileWriter(outputPath);
            fileWriter.write(out.toString());
            fileWriter.close();
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


}
