package com.server.controllers;

import com.server.models.Supplier;
import com.server.network.Response;

import java.util.HashMap;
import java.util.Map;

public class SupplierController {

    private static final Map<Integer, Supplier> suppliers = new HashMap<>();

    static {
        suppliers.put(1, new Supplier(1, "ООО Поставщик"));
        suppliers.put(2, new Supplier(2, "ЗАО ИмпортСнаб"));
    }

    public Response getAllSuppliers() {
        return new Response(true, "Список поставщиков", suppliers.values().toString());
    }
}
