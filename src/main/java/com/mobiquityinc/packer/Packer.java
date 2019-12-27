package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Thing;
import org.apache.commons.lang3.StringUtils;

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
            if (StringUtils.isNotEmpty(item)) {

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
        Thing current;
        Thing next;
        List<Thing> resultingPackage = new ArrayList<>();
        things = things.stream().sorted().collect(Collectors.toList());

        double totalWeightSum = things.stream().mapToDouble(t -> t.getWeight()).sum();
        if (totalWeightSum <= maxWeight) {
            return things;
        }

        for (int i = 0; i < things.size() - 1; i++) {
            current = things.get(i);
            if (totalWeight == 0) {
                totalWeight = current.getWeight();
                resultingPackage.add(current);
            }

            next = things.get(i + 1);
            // You would prefer to send a package which weighs less in case there is more than one package with the same price
            if (totalWeight + next.getWeight() > maxWeight || next.getCost() == current.getCost()) {
                continue;
            }
            totalWeight += next.getWeight();
            resultingPackage.add(next);
        }

        return resultingPackage;
    }
}
