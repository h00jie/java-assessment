package lib;

import gr.pricefox.interfaces.CarInsuranceProvider;
import gr.pricefox.services.MyService;
import gr.pricefox.services.OtherService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class DITest {
    private final DI di = new DI();

    @org.junit.jupiter.api.Test
    void oneOf() {
        MyService myService = di.oneOf(MyService.class);
        assertInstanceOf(MyService.class, myService);
    }

    @org.junit.jupiter.api.Test
    void oneOfFail() {
        MyService myService = di.oneOf(MyService.class);
        assertInstanceOf(OtherService.class, myService, "di.oneOf(MyService.class) must be of MyService type");
    }

    @org.junit.jupiter.api.Test
    void singletonOf() {
        OtherService myService = di.singletonOf(OtherService.class);
        assertInstanceOf(OtherService.class, myService);
    }

    @org.junit.jupiter.api.Test
    void singletonOfFail() {
        OtherService otherService = di.singletonOf(OtherService.class);
        assertInstanceOf(MyService.class, otherService, "di.singletonOf(OtherService.class) must be of OtherService type");
    }

    @org.junit.jupiter.api.Test
    void listOf() {
        List<CarInsuranceProvider> carInsuranceProviders = di.listOf(CarInsuranceProvider.class);

        assertEquals(3, carInsuranceProviders.size(), "There must be 3 implementations of CarInsuranceProvider");

        for (CarInsuranceProvider carInsuranceProvider : carInsuranceProviders) {
            assertTrue(carInsuranceProvider instanceof CarInsuranceProvider, "All carInsuranceProviders instances have tobe of CarInsuranceProvider implementation");
        }
    }

    @org.junit.jupiter.api.Test
    void listOfFailOnLength() {
        List<CarInsuranceProvider> carInsuranceProviders = di.listOf(CarInsuranceProvider.class);
        assertEquals(2, carInsuranceProviders.size(), "There must be 3 implementations of CarInsuranceProvider");
    }
}