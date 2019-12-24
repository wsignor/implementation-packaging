package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Thing;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
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
                String[] thingsToProcess = line.split(":");
                double maxWeight = Double.parseDouble(thingsToProcess[0].trim());

                /*
                List<Thing> things = Stream.of(thingsToProcess[1].split(" "))
                        .map(item -> thingConverter(item, maxWeight))
                        .collect(Collectors.toList());
                */

                List<Thing> things = buildPackage(thingsToProcess[1].split(" "), maxWeight);

                things = things.stream().sorted().collect(Collectors.toList());

                List<Thing> acceptedThings = checkBestFit(things, maxWeight);

                if (acceptedThings.isEmpty()) {
                    result.add("-");
                } else {
                    List<String> indexes = new ArrayList<>();
                    acceptedThings.stream().forEach(c -> indexes.add("" + c.getIndex()));
                    result.add(String.join(",", indexes));
                }
            }

            return result.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    // TODO remove if not used
    public static List<Thing> acceptedThings(double expectedWeight, Thing t) {
        List<Thing> chosenThings = new ArrayList<>();
        double weightSum = chosenThings.stream()
                .mapToDouble(ct -> ct.getWeight())
                .sum();
        double priceSum = chosenThings.stream()
                .mapToDouble(ct -> ct.getCost())
                .sum();

        if (priceSum > 0) {
            if (weightSum + t.getWeight() < expectedWeight) {
                chosenThings.add(t);
            } else {
                Optional<Thing> lowestPriceThing = chosenThings.stream()
                        .min(Comparator.comparing(Thing::getCost));

                if (t.getCost() == lowestPriceThing.get().getCost() &&
                        t.getWeight() < lowestPriceThing.get().getWeight()) {
                    chosenThings.remove(lowestPriceThing.get());
                }
            }
        } else {
            chosenThings.add(t);
        }
        return chosenThings;
    }

    // TODO remove if not used
    public static Thing thingConverter(String item, Double maxWeight) {
        if (StringUtils.isNotEmpty(item)) {
            String[] properties = item.split(",");

            Integer index = Integer.valueOf(properties[0].trim().substring(1));
            Double weight = Double.valueOf(properties[1]);
            Double cost = Double.valueOf(properties[2].substring(1, properties[2].length() - 1));

            Thing thing = new Thing(index, weight, cost);

            if (thing.getWeight() <= maxWeight) {
                return thing;
            }
        }
        return null;
    }

    public static List<Thing> buildPackage(String[] items, double maxWeight) throws APIException {
        List<Thing> validThings = new ArrayList<>();
        for (String item : items) {
            if (StringUtils.isNotEmpty(item)) {
                String[] properties = item.split(",");

                Integer index = Integer.valueOf(properties[0].trim().substring(1));
                Double weight = Double.valueOf(properties[1]);
                Double cost = Double.valueOf(properties[2].substring(1, properties[2].length() - 1));

                Thing thing = new Thing(index, weight, cost);

                if (thing.getCost() > 100) {
                    throw new APIException("Cost can't be higher than 100");
                }

                if (thing.getWeight() > 100) {
                    throw new APIException("Weight can't be higher than 100");
                }

                if (thing.getWeight() <= maxWeight) {
                    validThings.add(thing);
                }
            }
        }
        return validThings;
    }


    public static List<Thing> checkBestFit(List<Thing> things, Double maxWeight) {
        double totalWeight = 0.0;
        Thing current;
        Thing next;
        List<Thing> resultingPackage = new ArrayList<>();

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
