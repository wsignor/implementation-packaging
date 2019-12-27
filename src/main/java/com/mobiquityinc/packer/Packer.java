package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Thing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Packer {

    private Packer() {
    }

    public static String pack(String filePath) throws APIException {
        StringJoiner result = new StringJoiner("\n");

        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            Scanner scanner = new Scanner(inputStream, "UTF-8");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<Thing> things = buildPackage(line);

                if (things.isEmpty()) {
                    result.add("-");
                } else {
                    List<String> indexes = new ArrayList<>();
                    things.stream().forEach(c -> indexes.add("" + c.getIndex()));
                    result.add(String.join(",", indexes));
                }
            }

            return result.toString();
        } catch (FileNotFoundException fileException) {
            throw new APIException("File not found. " + fileException.getMessage());
        }
    }


    public static List<Thing> buildPackage(String line) {

        List<Thing> validThings = new ArrayList<>();
        String[] thingsToProcess = line.split(":");
        double maxWeight = Double.parseDouble(thingsToProcess[0].trim());
        String[] items = thingsToProcess[1].split(" ");

        if (items.length > 15 || maxWeight > 100) {
            return validThings;
        }

        for (String item : items) {
            if (!item.isEmpty()) {

                String[] properties = item.split(",");
                Thing thing = buildThing(properties);

                if (thing.getCost() > 100 || thing.getWeight() > 100 || thing.getWeight() > maxWeight) {
                    continue;
                }
                validThings.add(thing);
            }
        }
        return checkBestFit(validThings, maxWeight);
    }

    public static Thing buildThing(String[] properties) {
        Integer index = Integer.valueOf(properties[0].trim().substring(1));
        Double weight = Double.valueOf(properties[1]);
        Double cost = Double.valueOf(properties[2].substring(1, properties[2].length() - 1));
        return new Thing(index, weight, cost);
    }


    public static List<Thing> checkBestFit(List<Thing> things, Double maxWeight) {
        double totalWeight = 0.0;
        Thing currentThing;
        Thing nextThing;

        double totalWeightSum = things.stream().mapToDouble(t -> t.getWeight()).sum();
        if (Double.compare(totalWeightSum, maxWeight) <= 0) {
            return things;
        }

        things = things.stream().sorted().collect(Collectors.toList());
        List<Thing> resultingPackage = new ArrayList<>();
        for (int i = 0; i < things.size() - 1; i++) {
            currentThing = things.get(i);
            if (totalWeight == 0) {
                totalWeight = currentThing.getWeight();
                resultingPackage.add(currentThing);
            }

            nextThing = things.get(i + 1);
            if (totalWeight + nextThing.getWeight() > maxWeight && nextThing.compareTo(currentThing) > 0) {
                continue;
            }
            totalWeight += nextThing.getWeight();
            resultingPackage.add(nextThing);
        }

        return resultingPackage;
    }
}
