package com.example.parser.Controller;

import org.springframework.web.bind.annotation.*;

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
            return checkJson(jsonFiltered);
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

}
