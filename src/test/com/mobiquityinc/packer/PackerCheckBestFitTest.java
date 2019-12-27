package com.mobiquityinc.packer;

import com.mobiquityinc.model.Thing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackerCheckBestFitTest {

    @Test
    public void buildPackage_whenHappyPath_shouldReturnThings() {
        Thing thing = new Thing(1, 45.0, 22.0);
        Thing thing2 = new Thing(2, 4.0, 43.0);
        Thing thing3 = new Thing(3, 76.0, 62.0);

        List<Thing> things = new ArrayList<>(Arrays.asList(thing, thing2, thing3));
        List<Thing> resultingThings = Packer.checkBestFit(things, 99.0);

        Double resultingTotalSum = resultingThings.stream().mapToDouble(t -> t.getWeight()).sum();
        Assertions.assertTrue(resultingTotalSum <= 100);
        Assertions.assertTrue(resultingThings.get(0).getIndex().equals(3));
        Assertions.assertTrue(resultingThings.get(1).getIndex().equals(2));
    }

    @Test
    public void buildPackage_whenTotalSumWeightIsLessThanMaxWeight_shouldReturnSameList() {
        Thing thing = new Thing(1, 5.0, 22.0);
        Thing thing2 = new Thing(2, 4.0, 23.0);
        Thing thing3 = new Thing(3, 76.0, 50.0);

        List<Thing> things = new ArrayList<>(Arrays.asList(thing, thing2, thing3));
        List<Thing> resultingThings = Packer.checkBestFit(things, 99.0);

        Assertions.assertTrue(resultingThings.get(0).getIndex().equals(thing.getIndex()));
        Assertions.assertTrue(resultingThings.get(1).getIndex().equals(thing2.getIndex()));
        Assertions.assertTrue(resultingThings.get(2).getIndex().equals(thing3.getIndex()));
    }

    @Test
    public void buildPackage_whenSameCostWithDifferentWeigh_shouldPreferThingsThatWeighsLess() {
        Thing thingWithMoreWeigh = new Thing(1, 10.0, 10.0);
        Thing thingWithLessWeigh = new Thing(2, 4.0, 10.0);
        Thing thingHeavierThanLimit = new Thing(3, 13.0, 10.0);

        List<Thing> things = new ArrayList<>(Arrays.asList(thingWithMoreWeigh, thingWithLessWeigh, thingHeavierThanLimit));
        List<Thing> resultingThings = Packer.checkBestFit(things, 10.0);

        Assertions.assertTrue(resultingThings.get(0).getIndex().equals(thingWithLessWeigh.getIndex()));
    }
}
