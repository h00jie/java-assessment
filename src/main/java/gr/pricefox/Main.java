package gr.pricefox;

import gr.pricefox.interfaces.CarInsuranceProvider;
import gr.pricefox.services.MyService;
import gr.pricefox.services.OtherService;
import lib.DI;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        final DI di = new DI(); // this is the injector

        final OtherService otherService = di.oneOf(OtherService.class); // contstruct each time a new instance of OtherService
        final MyService myService = di.singletonOf(MyService.class); // construct a singleton (but not static) instance of MyService
        final List<CarInsuranceProvider> myShapres = di.listOf(CarInsuranceProvider.class); // construct a list of objects implementing the given interface
        System.out.println("CarInsuranceProvider lenght " + myShapres.size());
        // use the objectslistOf
    }
}