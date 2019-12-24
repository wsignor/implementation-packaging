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
                String[] thingsToProcess = line.split(":");
                double maxWeight = Double.parseDouble(thingsToProcess[0].trim());
                List<Thing> things = buildPackage(thingsToProcess[1], maxWeight);

                things = things.stream().sorted().collect(Collectors.toList());

                List<Thing> acceptedThings = checkBestFit(things, maxWeight);

                if (acceptedThings.isEmpty()) {
                    //System.out.println("-");
                    result.add("-");
                } else {

                    List<String> indexes = new ArrayList<>();
                    acceptedThings.stream().forEach(c -> indexes.add("" + c.getIndex()));

                    //System.out.println(String.join(",", indexes));
                    result.add(String.join(",", indexes));
                }
            }

            return result.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9)
     * (6,46.34,€48)
     * 8 : (1,15.3,€34)
     * 75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52)
     * (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)
     * 56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36)
     * (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)
     */

    public static List<Thing> buildPackage(String line, double maxWeight) throws APIException {
        String[] things = line.split(" +");

        List<Thing> validThings = new ArrayList<>();
        for (String thing : things) {
            if (StringUtils.isNotEmpty(thing)) {

                thing = thing.replace("(", "");
                thing = thing.replace(")", "");
                String[] values = thing.split(",");
                Integer index = Integer.valueOf(values[0]);
                Double weight = Double.valueOf(values[1]);
                Double cost = Double.valueOf(values[2].replace("€", ""));

                Thing thing1 = new Thing(index, weight, cost);

                if (thing1.getCost() > 100) {
                    throw new APIException("Cost can't be higher than 100");
                }

                if (thing1.getWeight() > 100) {
                    throw new APIException("Weight can't be higher than 100");
                }

                if (thing1.getWeight() <= maxWeight) {
                    validThings.add(thing1);
                }
            }
        }
        return validThings;
    }

    public static List<Thing> checkBestFit(List<Thing> things, Double maxWeight) {
        double totalW = 0.0;
        Thing actual;
        Thing next;
        List<Thing> resultingThings = new ArrayList<>();

        for (int i = 0; i < things.size() - 1; i++) {
            actual = things.get(i);

            if (totalW == 0) {
                totalW = actual.getWeight();
                resultingThings.add(actual);
            }

            next = things.get(i + 1);
            if (totalW + next.getWeight() > maxWeight || next.getCost() == actual.getCost()) {
                continue;
            }
            totalW += next.getWeight();
            resultingThings.add(next);
        }

        return resultingThings;
    }
}
