package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

public class PackerPackTest {

    @Test
    public void packThings_whenHappyPath_shouldReturnThings() throws Exception {

        String filePath = getClass().getClassLoader().getResource("things.txt").getPath();
        StringJoiner compare = new StringJoiner("\n");
        compare.add("4");
        compare.add("-");
        compare.add("2,7");
        compare.add("8,9");

        String result = Packer.pack(filePath);

        Assertions.assertEquals(compare.toString(), result);
    }

    @Test
    public void packThings_whenParamNotProvided_shouldReturnError() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Packer.pack(null);
        });
    }

    @Test
    public void packThings_whenFileDoesntExist_shouldThrowAPIException() {
        Assertions.assertThrows(APIException.class, () -> {
            Packer.pack("invalidPath");
        });
    }

    @Test
    public void packThings_whenInvalidThings_shouldReturnError() {
        String thingsWithInvalidFormat = getClass().getClassLoader().getResource("things-invalid-format.txt").getPath();

        Assertions.assertThrows(NumberFormatException.class, () -> {
            Packer.pack(thingsWithInvalidFormat);
        });
    }

    @Test
    public void packThings_whenEmptyFile_shouldReturnEmpty() throws Exception {
        String emptyFile = getClass().getClassLoader().getResource("things-empty.txt").getPath();
        String result = Packer.pack(emptyFile);

        Assertions.assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void packThings_whenComplexFile_shouldReturnExpectedThings() throws Exception {
        String complexFile = getClass().getClassLoader().getResource("things-complex-file.txt").getPath();
        StringJoiner compare = new StringJoiner("\n");

        compare.add("-");
        compare.add("-");
        compare.add("2,7");
        compare.add("8,9");
        compare.add("6");
        compare.add("1");
        compare.add("2,7");
        compare.add("8,6,9");
        compare.add("-");
        compare.add("-");
        compare.add("6,3");
        compare.add("8,9");
        compare.add("-");
        compare.add("-");
        compare.add("3");
        compare.add("9");
        compare.add("-");
        compare.add("1");
        compare.add("2,3");
        compare.add("9");
        compare.add("6");
        compare.add("1");
        String result = Packer.pack(complexFile);

        Assertions.assertEquals(compare.toString(), result);
    }

}
