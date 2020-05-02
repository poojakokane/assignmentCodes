package com.realEstateManagement;

import java.util.TreeMap;

public class Project4 {

    public static int transactionId;
    public static TreeMap<Integer, Property> databaseOfProperty;

    public static void main(String args[]){
        databaseOfProperty = new TreeMap<Integer, Property>();
        transactionId = 0;

        // Add some properties
        databaseOfProperty.put(transactionId++,
                new Property(
                        "propertyAddress1",
                        4,
                        2000,
                        400000)
        );
        databaseOfProperty.put(transactionId++,
                new Property(
                        "propertyAddress2",
                        3,
                        1800,
                        350000)
        );
        databaseOfProperty.put(transactionId++,
                new Property(
                        "propertyAddress3",
                        6,
                        5000,
                        2000000)
        );
        databaseOfProperty.put(transactionId++,
                new Property(
                        "propertyAddress4",
                        2,
                        1000,
                        200000)
        );

        // Print all the properties
        for(Property p : databaseOfProperty.values()){
            System.out.println(p);
        }

        System.out.println("Changing status of few properties");
        //Change status of some properties
        databaseOfProperty.get(2).changeState(RealEstateStatus.UNDER_CONTRACT);
        databaseOfProperty.get(3).changeState(RealEstateStatus.SOLD);

        // Print all the properties
        for(Property p : databaseOfProperty.values()){
            System.out.println(p);
        }

    }


}
