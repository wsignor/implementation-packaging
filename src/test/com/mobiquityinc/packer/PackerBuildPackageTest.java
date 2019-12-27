package com.mobiquityinc.packer;

import org.junit.jupiter.api.Test;

public class PackerBuildPackageTest {

    @Test
    public void buildPackage_whenHappyPath_shouldReturnThings() throws Exception {
        Packer.buildPackage(null, 0.0);
    }

}
