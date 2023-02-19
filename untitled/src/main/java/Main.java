import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static java.nio.file.Files.writeString;


class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileNameXML = "data.xml";

        List<Employee> list = parseCSV(columnMapping, fileName);
        List<Employee> list2 = null;
        try {
            list2 = parseXML(fileNameXML);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String json = listToJson(list);
        String json2 = listToJson(list2);

        String jsonFile = "data.json";
        String jsonFile2 = "data2.json";

        writeString(json, jsonFile);
        writeString(json2, jsonFile2);
    }

    public static List<Employee> parseXML(String fileName) throws Exception {
        List<Employee> list = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(fileName);
        Node staff = doc.getDocumentElement();
        NodeList staffNodeList = staff.getChildNodes();
        for (int i = 0; i < staffNodeList.getLength(); i++) {
            Node nodeEmployee = staffNodeList.item(i);
            if (Node.ELEMENT_NODE == nodeEmployee.getNodeType()) {
                Element element = (Element) nodeEmployee;
                long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                String country = element.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                list.add(new Employee(id, firstName, lastName, country, age));
            }
        }
        return list;
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

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }


    private static void writeString(String json, String json2) throws IOException {


        JSONObject obj = new JSONObject();
        String[] d = json.split("},", 2);
        obj.put("id1", d[0]);
        obj.put("id2", d[1]);

        try (FileWriter file = new FileWriter(json2)) {
            file.write(obj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


