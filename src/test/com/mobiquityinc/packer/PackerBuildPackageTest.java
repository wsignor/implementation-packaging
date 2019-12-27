package com.mobiquityinc.packer;

import com.mobiquityinc.model.Thing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PackerBuildPackageTest {

    @Test
    public void buildPackage_whenHappyPath_shouldReturnThings() {
        String line = "50 : (1,10,€45) (2,30,€98) (3,4.5,€3) (4,5.5,€76)";
        List<Thing> things = Packer.buildPackage(line);

        List<Integer> indexesResult = things.stream().map(Thing::getIndex).collect(Collectors.toList());
        List<Integer> expected = Arrays.asList(1, 2, 3, 4);
        Assertions.assertEquals(expected, indexesResult);
    }

    @Test
    public void buildPackage_whenAllWeightsHaveMoreThan100_shouldReturnNothing() {
        String line = "100 : (1,101,€45) (2,320,€98) (3,422.5,€3) (4,512.5,€76)";
        List<Thing> things = Packer.buildPackage(line);

        Assertions.assertTrue(things.isEmpty());
    }

    @Test
    public void buildPackage_whenMoreThan15Items_shouldReturnNothing() {
        String line = "100 : (1,101,€45) (2,320,€98) (3,422.5,€3) (4,512.5,€76) (5,512.5,€76) (6,512.5,€76) (7,512.5,€76) (8,512.5,€76) (9,512.5,€76) (10,512.5,€76) (11,512.5,€76) (12,512.5,€76) (13,512.5,€76) (14,512.5,€76) (15,512.5,€76) (16,512.5,€76) (17,512.5,€76)";
        List<Thing> things = Packer.buildPackage(line);

        Assertions.assertTrue(things.isEmpty());
    }

    @Test
    public void buildPackage_whenPackageWeightIsHigherThan100_shouldReturnNothing() {
        String line = "101 : (1,101,€45) (2,320,€98) (3,422.5,€3) (4,512.5,€76)";
        List<Thing> things = Packer.buildPackage(line);

        Assertions.assertTrue(things.isEmpty());
    }

    @Test
    public void buildPackage_whenAllItemsCostMoreThan100_shouldReturnNothing() {
        String line = "99 : (1,1016544.3216,€45321.65) (2,32,€98654.9555) (3,422.5,€30326.66542) (4,1.5,€222276)";
        List<Thing> things = Packer.buildPackage(line);

        Assertions.assertTrue(things.isEmpty());
    }

    @Test
    public void buildPackage_whenAllItemsCostMoreThan100_shouldReturnNothing1() {
        String line = "99 : (1,1016544.3216,€45321.65) (2,32,99.9555) (3,422.5,€30326.66542) (4,1.5,€222276)";
        List<Thing> things = Packer.buildPackage(line);

        List<Integer> indexesResult = things.stream().map(Thing::getIndex).collect(Collectors.toList());
        List<Integer> expected = Arrays.asList(2);
        Assertions.assertEquals(expected, indexesResult);
    }

}
