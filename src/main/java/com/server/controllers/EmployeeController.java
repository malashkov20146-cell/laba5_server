package com.server.controllers;

import com.server.models.Employee;
import com.server.network.Request;
import com.server.network.Response;

import java.util.HashMap;
import java.util.Map;

public class EmployeeController {

    private static final Map<Integer, Employee> employees = new HashMap<>();
    private static int nextId = 1;

    public Response login(Request request) {
        String[] parts = request.getData().split(";");
        String login = parts[0];
        String password = parts[1];

        for (Employee e : employees.values()) {
            if (e.getLogin().equals(login) && e.getPassword().equals(password))
                return new Response(true, "Авторизация успешна", null);
        }

        return new Response(false, "Неверный логин или пароль", null);
    }

    public Response register(Request request) {
        String[] parts = request.getData().split(";");
        String login = parts[0];
        String password = parts[1];

        Employee employee = new Employee(nextId++, login, password);
        employees.put(employee.getId(), employee);

        return new Response(true, "Сотрудник зарегистрирован", null);
    }

    public Response getAllEmployees() {
        return new Response(true, "Список сотрудников", employees.values().toString());
    }

    public Response deleteEmployee(Request request) {
        int id = Integer.parseInt(request.getData());

        if (!employees.containsKey(id))
            return new Response(false, "Сотрудник не найден", null);

        employees.remove(id);
        return new Response(true, "Сотрудник удалён", null);
    }

    public Response updateEmployee(Request request) {
        String[] parts = request.getData().split(";");
        int id = Integer.parseInt(parts[0]);
        String newLogin = parts[1];

        Employee employee = employees.get(id);
        if (employee == null)
            return new Response(false, "Сотрудник не найден", null);

        employee.setLogin(newLogin);
        return new Response(true, "Данные обновлены", null);
    }

    public Response readEmployee(Request request) {
        int id = Integer.parseInt(request.getData());

        Employee employee = employees.get(id);
        if (employee == null)
            return new Response(false, "Сотрудник не найден", null);

        return new Response(true, "Информация о сотруднике", employee.toString());
    }
}
