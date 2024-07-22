package com.example.parser.Controller;

import com.example.parser.Model.Person;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;

@RestController
@RequestMapping("/")
public class JsonParserController{
    @PostMapping("/convert")
    public boolean convertToJsonParse(@RequestBody String json) {
        String jsonBody = json.trim();
        String jsonFiltered = jsonBody.substring(1,jsonBody.length()-1).trim();
        System.out.println("json"+json.charAt(1));
        for(int i=0;i<jsonFiltered.length();i++){
            System.out.println(jsonFiltered.charAt(i));
        }
        if(jsonBody.startsWith("{") && jsonBody.endsWith("}")){
            boolean check = checkJson(jsonFiltered);
            if(check) {
                Person person = parser(jsonFiltered);
            }
            return check;
        }
        else {
            return false;
        }
    }

    public boolean checkJson(String jsonFiltered){
        String[] jsonElementList = jsonFiltered.split(",");

        for(String element : jsonElementList){
            String removedSpaces = element.trim();
            String[] keyAndValue = removedSpaces.split(":");
            if(keyAndValue.length == 2){
                if(keyAndValue[0].charAt(0) == '"' && keyAndValue[0].charAt(keyAndValue[0].length()-1) == '"') {
                    if (!((isNumberOrBracketsOrNull(keyAndValue[1].trim())) ||
                            (keyAndValue[1].trim().charAt(0) == '"' && keyAndValue[1].trim().charAt(keyAndValue[1].trim().length()-1) == '"'))) {
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        return true;
    }
    public static boolean isNumberOrBracketsOrNull(String str) {
        // Check if the string is null
        if (str == null) {
            return true;
        }

        // Regular expression pattern to match numbers, brackets, and empty string
        String regex = "^(-?\\d+(\\.\\d+)?|true|false|\\(\\)|\\{\\}|\\[\\]|null)$";
        boolean ans = str.matches(regex);
        return ans;
    }

    public Person parser(String jsonFiltered) {
        Person person = new Person();
        String[] jsonElementList = jsonFiltered.split(",");
        try {
            for (String element : jsonElementList) {
                String removedSpaces = element.trim();
                String[] keyAndValue = removedSpaces.split(":");
                Field field = Person.class.getDeclaredField(keyAndValue[0]);
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if(fieldType == int.class){
                    field.setInt(person,Integer.parseInt(keyAndValue[1]));
                }
                else if(fieldType == boolean.class) {
                    field.setBoolean(person, Boolean.parseBoolean(keyAndValue[1]));
                }
                else if(fieldType == String.class) {
                    field.set(person, keyAndValue[1].substring(1, keyAndValue.length));
                }
                else{
                    field.set(person, null);
                }

            }
        }
        catch (NoSuchFieldException |  IllegalAccessException e) {
            e.printStackTrace();
        }
        return person;

    }
}
