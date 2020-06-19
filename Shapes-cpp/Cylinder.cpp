//
//  Cylinder.cpp
//  MP4
//
//  Created by Daniel Colburn on 6/18/20.
//  Copyright © 2020 Daniel Colburn. All rights reserved.
//

#include "Cylinder.hpp"
#include "Circle.hpp"
#include "Point.hpp"
#include <iostream>
#include <cmath>

using namespace std;

Cylinder::Cylinder(double x, double y, double rad, double h){

}

Cylinder::Cylinder(Circle cir, double h){
    this->base = Circle(cir);
    this->height = h;
}

double Cylinder::getHeight()
{
    return height;
}

double Cylinder::surface_area() const {
    return ((2 * base.area() ) + (base.circumference() * height));
}

double Cylinder::volume() const {
    return base.area() * height;
}

void Cylinder::printData() const {
    cout << "Base = ";
    base.printData();
    cout << "Height = " << height << endl;
}

void Cylinder::inputData() {
    base.inputData();
    cout << "Enter height: ";
    cin >> height;
}
