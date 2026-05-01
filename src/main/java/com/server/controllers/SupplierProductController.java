package com.server.controllers;

import com.server.network.Request;
import com.server.network.Response;

public class SupplierProductController {

    public Response processSupplierProductRelationship(Request request) {
        return new Response(true, "Связь поставщик–товар обработана", null);
    }
}
