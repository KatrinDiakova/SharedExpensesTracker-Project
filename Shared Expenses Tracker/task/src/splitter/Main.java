package splitter;


import java.util.*;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            String input = sc.nextLine();
            List<String> inputList = Arrays.asList(input.split(" "));
            UserInputParser.parseUserInput(inputList);
        }
    }
}
