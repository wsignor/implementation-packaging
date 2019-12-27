package com.mobiquityinc.packer;

import org.junit.jupiter.api.Test;

public class PackerCheckBestFitTest {

    @Test
    public void buildPackage_whenHappyPath_shouldReturnThings() throws Exception {
        Packer.checkBestFit(null, 0.0);
    }
}
