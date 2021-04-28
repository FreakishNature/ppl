package com.testingAPI.backend.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvUtils {
    public static List<Map<String,String>> csvToObject(String src) throws IOException {
        List<Map<String,String>> dataframe = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(src));
        String line;
        List<String> fields = null;

        for(int i =0 ; (line =br.readLine()) != null; i++){
            String[] values = line.split(",");
            if(i == 0){
                fields = Arrays.asList(values);
                continue;
            }
            Map<String,String> row = new LinkedHashMap<>();
            for(int j = 0; j < fields.size(); j++){
                row.put(fields.get(j),(j >= values.length ? "" : values[j]));
            }
            dataframe.add(row);
        }

        return dataframe;
    }

    private static String spaces(int length){
        char[] charArray = new char[length];
        Arrays.fill(charArray,' ');
        return new String(charArray);
    }

    public static String csvToCucumberTable(String src, int amountOfAddedSpaces) throws IOException {
        List<Map<String,String>> dataframe = csvToObject(src);
        if(dataframe.size() == 0) { throw new IOException("No data in file"); }

        List<String> keyList = new ArrayList<>(dataframe.get(0).keySet());

        StringBuilder header = new StringBuilder();
        header.append("|");
        for(int i = 0 ; i < keyList.size(); i++){
            int cellWidth = keyList.get(i).length();

            int widthToAddForEachSide = (amountOfAddedSpaces > cellWidth) ? (amountOfAddedSpaces - cellWidth) / 2 : 0;
            String additionalSpaces = spaces(widthToAddForEachSide);
            String cell = additionalSpaces + keyList.get(i).replace("|","\\|") + ( ((amountOfAddedSpaces - cellWidth)));
            if(i == keyList.size() - 1){
                header.append(cell);
            } else {
                header.append(cell).append("|");
            }
        }
        header.append("|\n");
        StringBuilder table = new StringBuilder();
        for(Map<String,String> dataframeRow : dataframe){
            StringBuilder row = new StringBuilder();
            for(int i = 0; i < keyList.size(); i++){
                String cell = dataframeRow.get(keyList.get(i)).replace("|","\\|");
                int cellWidth = cell.length();
                int widthToAddForEachSide = (amountOfAddedSpaces > cellWidth) ? (amountOfAddedSpaces - cellWidth) / 2 : 0;
                String addionalSpaces = spaces(widthToAddForEachSide);
                cell = addionalSpaces + dataframeRow.get(keyList.get(i)).replace("|","\\|") + ( ((amountOfAddedSpaces - cellWidth)) % 2 == 1 ? " " : "") + addionalSpaces;
                if(i == keyList.size() - 1){
                    row.append(cell);
                } else {
                    row.append(cell + "|");
                }

                table.append("|" + row.toString() + "|\n");
            }
        }
        return header.toString() + table.toString();
    }
}
