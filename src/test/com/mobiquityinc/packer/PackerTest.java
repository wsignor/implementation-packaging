package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.junit.jupiter.api.Test;

public class PackerTest {

    @Test
    public void test_whenHappyPath() throws APIException {
        String response = Packer.pack("fileLocation");
    }
}
