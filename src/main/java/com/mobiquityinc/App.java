package com.mobiquityinc;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.Packer;

public class App {
    public static void main(String[] args) throws APIException {
        try {
            String result = Packer.pack("C:\\Users\\wsignor\\Downloads\\MobEu-Hiring-Java\\MobEu-Hiring-Java\\src\\main\\resources\\things.txt");

            System.out.println(result);
        } catch (APIException e) {
            throw new APIException("File not found");
        }
    }
}
