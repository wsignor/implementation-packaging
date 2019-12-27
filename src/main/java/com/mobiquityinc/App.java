package com.mobiquityinc;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.Packer;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws APIException {

        Scanner console = new Scanner(System.in);
        System.out.println("Please specify the file path?");
        String filePath = console.nextLine();

        String result = Packer.pack(filePath);
        System.out.println(result);

    }

}
