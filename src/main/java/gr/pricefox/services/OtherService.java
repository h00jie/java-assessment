package gr.pricefox.services;

import gr.pricefox.implementions.CarInsuranceProviderImplementationA;
import gr.pricefox.interfaces.CarInsuranceProvider;

public class OtherService {

    private final CarInsuranceProvider carInsuranceProvider;

    public OtherService(CarInsuranceProviderImplementationA carInsuranceProvider) {
        this.carInsuranceProvider = carInsuranceProvider;
    }
}
