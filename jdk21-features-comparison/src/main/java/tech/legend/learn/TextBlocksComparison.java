package tech.legend.learn;

public class TextBlocksComparison {

    public static void main(String[] args) {
        System.out.println("=== Text Blocks Comparison ===");

        // JDK 8: Concatenation / Escaping
        String jsonOld = "{\n" +
                "  \"name\": \"Bob\",\n" +
                "  \"age\": 25\n" +
                "}";
        System.out.println("JDK 8 String:\n" + jsonOld);

        // JDK 21 (introduced in 13/15): Text Blocks
        String jsonNew = """
                {
                  "name": "Bob",
                  "age": 25
                }""";
        System.out.println("JDK 21 Text Block:\n" + jsonNew);
    }
}
