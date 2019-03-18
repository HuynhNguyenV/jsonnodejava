package com.softech;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExampleMain {

    private static final String FILENAME = "/Users/macintoshhd/IdeaProjects/jackson-tree-traversing-example/src/test.json";

    public static void main(String[] args) throws IOException {

        BufferedReader br = null;

//        String inputJson = "{\"name\":\"Jake\",\"salary\":3000,\"phones\":"
//                + "[{\"phoneType\":\"cell\",\"phoneNumber\":\"111-111-111\"},"
//                + "{\"phoneType\":\"work\",\"phoneNumber\":\"222-222-222\"}],"
//                +"\"taskIds\":[11,22,33],"
//                + "\"address\":{\"street\":\"101 Blue Dr\",\"city\":\"White Smoke\"}}";

        br = new BufferedReader(new FileReader(FILENAME));

        String inputJson = br.readLine();
//        System.out.println("input json: " + inputJson);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(inputJson);

//        System.out.printf("root: %s type=%s%n", rootNode, rootNode.getNodeType());
        traverse(rootNode, 1);

    }

    private static void traverse(JsonNode node, int level){
        if (node.getNodeType() == JsonNodeType.ARRAY){
            traverseArray(node, level);
        } else if (node.getNodeType() == JsonNodeType.OBJECT) {
            traverseObject(node, level);
        } else {
            throw new RuntimeException("Not yet implemented");
        }
    }

    private static void traverseObject(JsonNode node, int level) {
        node.fieldNames().forEachRemaining((String fieldName) -> {
            JsonNode childNode = node.get(fieldName);
            printNode(childNode, fieldName, level);
            // for nested object or array
            if (traversable(childNode)){
                traverse(childNode, level + 1);
            }
        });
    }

    private static void traverseArray(JsonNode node, int level){
        for (JsonNode jsonArrayNode : node) {
            printNode(jsonArrayNode, "arrayElement", level);
            if (traversable(jsonArrayNode)) {
                traverse(jsonArrayNode, level + 1);
            }
        }
    }

    private static boolean traversable(JsonNode node){
        return node.getNodeType() == JsonNodeType.OBJECT || node.getNodeType() == JsonNodeType.ARRAY;
    }

    private static void printNode(JsonNode node, String keyName, int level){
        if (traversable(node)) {
            System.out.printf("%" + (level * 4 - 3) + "s|-- %s=%s type=%s%n",
                    "", keyName, node.toString(), node.getNodeType());
        } else {
            Object value = null;
            if (node.isTextual()) {
                value = node.textValue();
            } else if (node.isNumber()) {
                value = node.numberValue();
            }
            System.out.printf("%" + (level * 4 - 3) + "s|-- %s=%s type=%s%n",
                    "", keyName, value, node.getNodeType());
        }
    }

}
