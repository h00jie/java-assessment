package gr.pricefox.services;

import gr.pricefox.implementions.CarInsuranceProviderImplementationB;
import gr.pricefox.interfaces.CarInsuranceProvider;

import java.util.logging.Logger;

public class MyService {

    private final CarInsuranceProvider carInsuranceProvider;

    public MyService(CarInsuranceProviderImplementationB carInsuranceProvider) {
        this.carInsuranceProvider = carInsuranceProvider;
    }
}
