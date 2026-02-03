/**
 * 
 */
package za.co.sindi.ai.mcp.test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates how to split a JSON array into individual JSON objects using JSON-B (JSON Binding)
 */
public class JsonbArraySplitter {

    /**
     * Splits a JSON array string into individual JSON object strings using JSON-B
     * 
     * @param jsonArrayString The JSON array string to split
     * @return An array of individual JSON object strings
     * @throws Exception If there's an error during JSON processing
     */
    public static String[] splitJsonArrayWithJsonb(String jsonArrayString) throws Exception {
        // Create Jsonb instance with default configuration
        Jsonb jsonb = JsonbBuilder.create();
        
        // Parse the JSON array string into a List of Object
        // Each element could be a JsonObject, String, Number, Boolean, etc.
        List<?> objectList = jsonb.fromJson(jsonArrayString, List.class);
        
        // Create array to hold the individual JSON strings
        String[] result = new String[objectList.size()];
        
        // Convert each object back to its JSON string representation
        for (int i = 0; i < objectList.size(); i++) {
            result[i] = jsonb.toJson(objectList.get(i));
        }
        
        return result;
    }
    
//    /**
//     * Alternative approach using generified type for more specific object mapping
//     * 
//     * @param jsonArrayString The JSON array string to split
//     * @param elementClass The class of elements contained in the array
//     * @return An array of individual JSON object strings
//     * @throws Exception If there's an error during JSON processing
//     */
//    public static <T> String[] splitJsonArrayWithJsonb(String jsonArrayString, Class<T> elementClass) throws Exception {
//        // Create a custom JsonbConfig
//        JsonbConfig config = new JsonbConfig().withFormatting(true);
//        Jsonb jsonb = JsonbBuilder.create(config);
//        
//        // Parse into a List of the specified type
//        List<T> typedList = jsonb.fromJson(
//            jsonArrayString, 
//            new jakarta.json.bind.annotation.JsonbTypeInfo.GenericType<List<T>>() {}
//        );
//        
//        // Convert each typed object to JSON
//        String[] result = new String[typedList.size()];
//        for (int i = 0; i < typedList.size(); i++) {
//            result[i] = jsonb.toJson(typedList.get(i));
//        }
//        
//        return result;
//    }
    
    /**
     * Example of a simple POJO class that could be used with the typed version
     */
    public static class Person {
        private int id;
        private String name;
        private List<String> skills;
        
        // Getters and setters required for JSON-B
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
        
        @Override
        public String toString() {
            return "Person{id=" + id + ", name='" + name + "', skills=" + skills + "}";
        }
    }
    
    /**
     * Example usage of the JSON-B array splitter
     */
    public static void main(String[] args) {
        try {
            // Example JSON array
            String jsonArray = """
                [
                  {"id": 1, "name": "John", "skills": ["Java", "SQL"]},
                  {"id": 2, "name": "Mary", "skills": ["Python", "JavaScript"]},
                  {"id": 3, "name": "Bob", "nested": {"key": "value", "array": [1, 2, 3]}},
                  {"id": 4, "name": "Alice", "note": "This has a \\"quote\\" inside"}
                ]
                """;
            
            System.out.println("Example 1: Generic approach (no type information)");
            String[] objects = splitJsonArrayWithJsonb(jsonArray);
            for (int i = 0; i < objects.length; i++) {
                System.out.println("\nObject " + (i+1) + ":");
                System.out.println(objects[i]);
            }
            
            // Example with simple array
            String simpleArray = "[\"one\", \"two\", \"three\"]";
            System.out.println("\nExample 2: Simple string array");
            String[] simpleObjects = splitJsonArrayWithJsonb(simpleArray);
            for (String obj : simpleObjects) {
                System.out.println(obj);
            }
            
//            // Example with typed array for specific element type
//            System.out.println("\nExample 3: Typed approach with Person class");
//            String personArray = """
//                [
//                  {"id": 1, "name": "John", "skills": ["Java", "SQL"]},
//                  {"id": 2, "name": "Mary", "skills": ["Python", "JavaScript"]}
//                ]
//                """;
//            
//            String[] personObjects = splitJsonArrayWithJsonb(personArray, Person.class);
//            for (String personJson : personObjects) {
//                System.out.println(personJson);
//                
//                // We can also parse it back to a Person object
//                Person person = JsonbBuilder.create().fromJson(personJson, Person.class);
//                System.out.println("As object: " + person);
//            }
            
        } catch (Exception e) {
            System.err.println("Error processing JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

