package com.realEstateManagement;

public class Property implements StateChangeable<RealEstateStatus>{
    private String propertyAddress;
    private int numberOfBedrooms;
    private int squareFootage;
    private int price;
    private RealEstateStatus realEstateStatus;

    public Property(String propertyAddress, int numberOfBedrooms, int squareFootage, int price){
        this.propertyAddress = propertyAddress;
        this.numberOfBedrooms = numberOfBedrooms;
        this.squareFootage = squareFootage;
        this.price = price;
        this.realEstateStatus = RealEstateStatus.FOR_SALE;
    }

    public void changeState(RealEstateStatus realEstateStatus) {
        this.realEstateStatus = realEstateStatus;
    }

    @Override
    public String toString() {
        return "Property{" +
                "propertyAddress='" + propertyAddress + '\'' +
                ", numberOfBedrooms=" + numberOfBedrooms +
                ", squareFootage=" + squareFootage +
                ", price=" + price +
                ", realEstateStatus=" + realEstateStatus.toString() +
                '}';
    }
}
