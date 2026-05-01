package com.server.controllers;

import com.server.models.Product;
import com.server.network.Request;
import com.server.network.Response;

import java.util.HashMap;
import java.util.Map;

public class ProductController {

    private static final Map<String, Product> products = new HashMap<>();

    public Response createProduct(Request request) {
        String[] parts = request.getData().split(";");
        String name = parts[0];
        int amount = Integer.parseInt(parts[1]);

        if (products.containsKey(name)) {
            return new Response(false, "Товар уже существует", null);
        }

        products.put(name, new Product(name, amount));
        return new Response(true, "Товар добавлен", null);
    }

    public Response getAllProducts() {
        if (products.isEmpty())
            return new Response(true, "Список товаров пуст", "[]");

        return new Response(true, "Список товаров", products.values().toString());
    }

    public Response getProductInfo(Request request) {
        String name = request.getData();

        Product product = products.get(name);
        if (product == null)
            return new Response(false, "Товар не найден", null);

        return new Response(true, "Информация о товаре", product.toString());
    }

    public Response updateProduct(Request request) {
        String[] parts = request.getData().split(";");
        String name = parts[0];
        int amount = Integer.parseInt(parts[1]);

        Product product = products.get(name);
        if (product == null)
            return new Response(false, "Товар не найден", null);

        product.setAmount(amount);
        return new Response(true, "Количество обновлено", null);
    }

    public Response deleteProduct(Request request) {
        String name = request.getData();

        if (!products.containsKey(name))
            return new Response(false, "Товар не найден", null);

        products.remove(name);
        return new Response(true, "Товар удалён", null);
    }

    public Response getSuppliers(Request request) {
        return new Response(true, "Поставщики найдены (заглушка)", null);
    }
}
