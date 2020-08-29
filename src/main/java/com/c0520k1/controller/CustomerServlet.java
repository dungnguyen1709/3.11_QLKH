package com.c0520k1.controller;

import com.c0520k1.model.Customer;
import com.c0520k1.service.CustomerService;
import com.c0520k1.service.CustomerServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerServlet",urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    private CustomerService customerService = new CustomerServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                createCustomer(request,response);
                break;
            case "edit":
                updateCustomer(request,response);
                break;
            case "delete":
                deleteCustomer(request,response);
                break;
            default:
                break;
        }
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id =  Integer.parseInt(request.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher dispatcher;

        if (customer == null) {
            dispatcher = request.getRequestDispatcher("WEB-INF/views/error-404.jsp");
        } else {
            this.customerService.remove(id);
        }
        response.sendRedirect("/customers");
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        Customer customer = this.customerService.findById(id);
        RequestDispatcher dispatcher;

        if (customer == null) {
            dispatcher = request.getRequestDispatcher("WEB-INF/views/error-404.jsp");
        } else {
            customer.setName(name);
            customer.setEmail(email);
            customer.setAddress(address);
            this.customerService.update(id,customer);
            request.setAttribute("customer",customer);
            request.setAttribute("message","Customer information was updated");
            dispatcher = request.getRequestDispatcher("WEB-INF/views/customer/edit.jsp");
        }
        dispatcher.forward(request,response);
    }

    private void createCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        int id = (int) (Math.random()* 10000);

        Customer customer = new Customer(id,name,email,address);
        this.customerService.save(customer);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/customer/create.jsp");
        request.setAttribute("message","New customer was created");
        dispatcher.forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                showCreateForm(request,response);
                break;
            case "edit":
                showEditForm(request,response);
                break;
            case "delete":
                showDeleteForm(request,response);
                break;
            case "view":
                break;
            default:
                listCustomers(request, response);
                break;
        }
    }

    private void showDeleteForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher dispatcher;

        if (customer == null) {
            dispatcher = request.getRequestDispatcher("WEB-INF/views/error-404.jsp");
        } else {
            request.setAttribute("customer",customer);
            dispatcher = request.getRequestDispatcher("WEB-INF/views/customer/delete.jsp");
        }

        dispatcher.forward(request,response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher dispatcher;

        if (customer == null) {
            dispatcher = request.getRequestDispatcher("WEB-INF/views/error-404.jsp");
        } else {
            request.setAttribute("customer",customer);
            dispatcher = request.getRequestDispatcher("WEB-INF/views/customer/edit.jsp");
        }
        dispatcher.forward(request,response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/customer/create.jsp");
        dispatcher.forward(request,response);
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Customer> customers = this.customerService.findAll();
        request.setAttribute("customers", customers);

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/customer/list.jsp");
        dispatcher.forward(request,response);
    }
}
