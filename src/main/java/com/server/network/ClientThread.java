package com.server.network;

import com.server.controllers.*;
import com.server.enums.Operation;
import com.server.exceptions.ResponseException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread {

    private final Socket clientSocket;

    private final ProductController productController;
    private final EmployeeController employeeController;
    private final SupplierController supplierController;
    private final DepartmentController departmentController;
    private final SupplierProductController supplierProductController;

    public ClientThread(Socket socket) {
        this.clientSocket = socket;

        productController = new ProductController();
        employeeController = new EmployeeController();
        supplierController = new SupplierController();
        departmentController = new DepartmentController();
        supplierProductController = new SupplierProductController();
    }

    public void run() {
        try (
                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            boolean keepRunning = true;

            while (keepRunning) {
                try {
                    Request request = (Request) input.readObject();

                    if (request != null) {
                        Response response = processRequest(request);

                        if (request.getOperation() == Operation.DISCONNECT) {
                            keepRunning = false;
                        }

                        output.writeObject(response);
                        output.flush();
                    } else {
                        output.writeObject(new Response(false, "Invalid request", null));
                        output.flush();
                    }

                } catch (IOException e) {
                    System.err.println("Connection error: " + e.getMessage());
                    keepRunning = false;
                } catch (ClassNotFoundException e) {
                    System.err.println("Class not found: " + e.getMessage());
                    keepRunning = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    keepRunning = false;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    private Response processRequest(Request request) {
        try {
            return switch (request.getOperation()) {

                case CREATE_PRODUCT -> productController.createProduct(request);
                case READ_PRODUCT -> productController.getProductInfo(request);
                case UPDATE_PRODUCT -> productController.updateProduct(request);
                case DELETE_PRODUCT -> productController.deleteProduct(request);

                case CREATE_EMPLOYEE -> employeeController.register(request);
                case READ_EMPLOYEE -> employeeController.readEmployee(request);
                case UPDATE_EMPLOYEE -> employeeController.updateEmployee(request);
                case DELETE_EMPLOYEE -> employeeController.deleteEmployee(request);

                case GET_ALL_SUPPLIERS -> supplierController.getAllSuppliers();
                case GET_ALL_PRODUCTS -> productController.getAllProducts();
                case GET_ALL_EMPLOYEES -> employeeController.getAllEmployees();
                case GET_ALL_DEPARTMENTS -> departmentController.getAllDepartments();

                case GET_SUPPLIERS_BY_PRODUCT -> productController.getSuppliers(request);

                case LINK_SUPPLIER_PRODUCT, UNLINK_SUPPLIER_PRODUCT ->
                        supplierProductController.processSupplierProductRelationship(request);

                case LOGIN -> employeeController.login(request);
                case REGISTER -> employeeController.register(request);

                case DISCONNECT -> new Response(true, "Disconnected", null);

                default -> new Response(false, "Unknown operation", null);
            };

        } catch (ResponseException e) {
            return new Response(false, e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Server error", null);
        }
    }

    private void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
