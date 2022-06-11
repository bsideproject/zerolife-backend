package com.bside.pjt.zerobackend.common.oauth;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRegistrationRepository {

    private final Map<String, ClientRegistration> registrationMap;

    public ClientRegistrationRepository(Map<String, ClientRegistration> registrationMap) {
        this.registrationMap = registrationMap;
    }

    public ClientRegistrationRepository(List<ClientRegistration> registrations) {
        this(createRegistrationsMap(registrations));
    }

    public ClientRegistration findByRegistrationId(String registrationId) {
        return this.registrationMap.get(registrationId);
    }

    public static Map<String, ClientRegistration> createRegistrationsMap(List<ClientRegistration> clientRegistrations) {
        return toUnmodifiableConcurrentMap(clientRegistrations);
    }

    private static Map<String, ClientRegistration> toUnmodifiableConcurrentMap(List<ClientRegistration> registrations) {
        ConcurrentHashMap<String, ClientRegistration> result = new ConcurrentHashMap<>();
        for (ClientRegistration clientRegistration : registrations) {
            result.put(clientRegistration.getRegistrationId(), clientRegistration);
        }
        return Collections.unmodifiableMap(result);
    }
}
