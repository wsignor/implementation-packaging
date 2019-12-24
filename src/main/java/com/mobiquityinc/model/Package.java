package com.mobiquityinc.model;

import lombok.Data;

import java.util.List;

@Data
public class Package {
    private List<Thing> things;
    private Double weightLimit;
}
