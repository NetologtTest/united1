import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.Files.writeString;


class Main {
    public static void main(String[] args) throws IOException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);

        writeString(json);

    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) throws IOException {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Employee> staff = csv.parse();

            return staff;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String listToJson(List<Employee> list) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        writeString(json);

        return json;


    }

    private static void writeString(String json) throws IOException {


        JSONObject obj = new JSONObject();
        String[] d = json.split("},", 2);
        obj.put("id1", d[0]);
        obj.put("id2", d[1]);

        try (FileWriter file = new FileWriter("data.json")) {
            file.write(obj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}





