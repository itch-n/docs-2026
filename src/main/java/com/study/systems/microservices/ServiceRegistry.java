package com.study.systems.microservices;

import java.util.*;
import java.util.List;
import java.util.Optional;

public interface ServiceRegistry {

    /**
     * Register a new service instance on startup or after a health-check recovery.
     *
     * TODO: Persist the instance so that lookup() can return it.
     * Consider: what happens if the same id is registered twice?
     */
    void register(ServiceInstance instance);

    /**
     * Deregister a service instance on graceful shutdown.
     *
     * @param instanceId the id field from the ServiceInstance to remove
     *
     * TODO: Remove the instance from the registry.
     * Consider: what if the id does not exist? (idempotent is preferable)
     */
    void deregister(String instanceId);

    /**
     * Return all healthy instances of a named service.
     *
     * @param serviceName the logical service name (e.g. "order-service")
     * @return list of currently registered instances; empty list if none healthy
     *
     * TODO: Filter the registry to instances whose serviceName matches.
     * TODO: Optionally filter by health status if instances carry a health field.
     * Consider: what should callers do when the returned list is empty?
     */
    List<ServiceInstance> lookup(String serviceName);
}
