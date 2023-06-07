package splitter;

import java.util.*;
import java.util.regex.*;

// дублирование кода
public class Group {

    static Map<String, List<String>> groupMap = new HashMap<>();

    // длинный метод
    public void parceData(List<String> input) {
        runSafely(() -> {
            String command = input.get(1);
            Pattern pattern = Pattern.compile("[A-Z]+\\b");
            Matcher matcher = pattern.matcher(input.get(2));
            String groupName = "";
            while (matcher.find()) {
                groupName = matcher.group();
            }

            Set<String> temporary = new HashSet<>();
            Pattern namePattern = Pattern.compile("(?:(?<=\\+)|(?<![-\\w]))[A-Z]+(?!\\w)", Pattern.CASE_INSENSITIVE);
            for (int i = 3; i < input.size(); i++) {
                Matcher matcherAdd = namePattern.matcher(input.get(i));
                while (matcherAdd.find()) {
                    if (matcherAdd.group().matches("[A-Z]+\\b")) {
                        List<String> names = groupMap.get(matcherAdd.group());
                        temporary.addAll(names);
                    } else {
                        temporary.add(matcherAdd.group());
                    }
                }
            }

            Pattern patternName = Pattern.compile("(?:(?<=\\-))[A-Z]+(?!\\w)", Pattern.CASE_INSENSITIVE);
            for (int i = 3; i < input.size(); i++) {
                Matcher matcherRemove = patternName.matcher(input.get(i));
                while (matcherRemove.find()) {
                    if (matcherRemove.group().matches("[A-Z]+\\b")) {
                        List<String> names = groupMap.get(matcherRemove.group());
                        names.forEach(temporary::remove);
                    } else {
                        temporary.remove(matcherRemove.group());
                    }
                }
            }
            switch (command) {
                case "create" -> createGroup(temporary, groupName);
                case "add" -> changeGroup(temporary, groupName);
                case "remove" -> removeFromGroup(temporary, groupName);
                case "show" -> showGroup(groupName);
                default -> throw new IllegalArgumentException();
            }
        });
    }

    private void createGroup(Set<String> temporary, String groupName) {
        runSafely(() -> {
            Pattern pattern = Pattern.compile("[A-Z]+\\b");
            if (pattern.matcher(groupName).matches()) {
                List<String> names = new ArrayList<>(temporary);
                Collections.sort(names);
                groupMap.putIfAbsent(groupName, names);
            } else {
                throw new IllegalArgumentException();
            }
        });
    }

    private void changeGroup(Set<String> temporary, String groupName) {
        if (groupMap.containsKey(groupName)) {
            List<String> names = groupMap.get(groupName);
            names.addAll(temporary);
            Collections.sort(names);
            groupMap.put(groupName, names);
        } else {
            System.out.println("Group doesn't exist");
        }
    }

    private void removeFromGroup(Set<String> temporary, String groupName) {
        if (groupMap.containsKey(groupName)) {
            List<String> names = groupMap.get(groupName);
            names.removeAll(temporary);
            Collections.sort(names);
            groupMap.put(groupName, names);
        } else {
            System.out.println("Group doesn't exist");
        }
    }

    private void showGroup(String groupName) {
        runSafely(() -> {
            if (!groupMap.containsKey(groupName)) {
                System.out.println("Unknown group");
            } else {
                List<String> names = groupMap.get(groupName);
                if (names.isEmpty()) {
                    System.out.println("Group is empty");
                } else {
                    for (String name : names) {
                        System.out.print(name);
                        System.out.println();
                    }
                }
            }
        });
    }

    private void runSafely(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            System.out.println("Illegal command arguments");
        }
    }
}





