package com.mobiquityinc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Thing implements Comparable<Thing> {
    private Integer index;
    private Double weight;
    private Double cost;

    @Override
    public int compareTo(Thing o) {
        int compareTo = o.getCost().compareTo(this.getCost());
        return compareTo != 0 ? compareTo : this.getWeight().compareTo(o.getWeight());
    }
}
