package com.server.controllers;

import com.server.models.Department;
import com.server.network.Response;

import java.util.HashMap;
import java.util.Map;

public class DepartmentController {

    private static final Map<Integer, Department> departments = new HashMap<>();

    static {
        departments.put(1, new Department(1, "Склад"));
        departments.put(2, new Department(2, "Логистика"));
        departments.put(3, new Department(3, "Бухгалтерия"));
    }

    public Response getAllDepartments() {
        return new Response(true, "Список отделов", departments.values().toString());
    }
}
