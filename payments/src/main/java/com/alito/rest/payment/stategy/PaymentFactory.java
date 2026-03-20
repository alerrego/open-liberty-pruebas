package com.alito.rest.payment.stategy;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class PaymentFactory {
    @Inject
    @Any //ESTO ES PQ COMO TENEMOS VARIOS STRATEGY CUANDO SE HAGA LA INYECCION, NO PUEDE ELEGIR ENTRE EJ 3 STRATEGY, ESTO HACE QUE SE INYECTEN LOS 3 Y LISTO.
    Instance<PaymentStrategy> strategys;

    public PaymentStrategy getStrategy(String providerName){
        for(PaymentStrategy strategy : strategys){
            if(strategy.getProviderName().equalsIgnoreCase(providerName)){
                return strategy;
            }
        }
        throw new WebApplicationException("Payment method not supported: " + providerName, 400);
    }
}
