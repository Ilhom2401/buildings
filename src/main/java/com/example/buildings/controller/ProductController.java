package com.example.buildings.controller;

import com.example.buildings.dto.ProductDto;
import com.example.buildings.dto.ProjectDto;
import com.example.buildings.entity.Employee;
import com.example.buildings.entity.Product;
import com.example.buildings.entity.Project;
import com.example.buildings.entity.Unit;
import com.example.buildings.service.EmployeeService;
import com.example.buildings.service.ProductService;
import com.example.buildings.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final EmployeeService employeeService;
    private final UnitService unitService;

    @PostMapping("/get-products")
    public String getList(@ModelAttribute("userId") Long userId, Model model){
        boolean access = employeeService.checkAccess("GET_LIST_PRODUCT", userId);
        if (!access)
            return "not-found";
        Employee employee = employeeService.getById(userId);
        List<Product> list = productService.getListByProjectId(employee.getProject().getId());
        model.addAttribute("list", list);
        model.addAttribute("projectName", employee.getProject().getName());
        model.addAttribute("empId", userId);
        return "product";
    }

    @PostMapping("/add-product")
    public String addProject(@ModelAttribute("userId") Long userId, Model model){
        boolean access = employeeService.checkAccess("ADD_PRODUCT", userId);
        if (!access)
            return "not-found";
        List<Unit> list = unitService.getList();
        model.addAttribute("empId", userId);
        model.addAttribute("unitList", list);
        return "add-product";
    }

    @PostMapping("/save-product")
    public String save(@ModelAttribute ProductDto productDto, Model model){
        boolean access = employeeService.checkAccess("ADD_PRODUCT", productDto.getUserId());
        if (!access)
            return "not-found";
        productService.save(productDto);
        Employee employee = employeeService.getById(productDto.getUserId());
        List<Product> list = productService.getListByProjectId(employee.getProject().getId());
        model.addAttribute("list", list);
        model.addAttribute("projectName", employee.getProject().getName());
        model.addAttribute("empId", productDto.getUserId());
        return "product";
    }

    @PostMapping("/edit-product")
    public String edit(@ModelAttribute ProductDto productDto, Model model){
        boolean access = employeeService.checkAccess("EDIT_PRODUCT", productDto.getUserId());
        if (!access)
            return "not-found";
        Product product = productService.getById(productDto.getProductId());
        model.addAttribute("empId", productDto.getUserId());
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/edited-product")
    public String edited(@ModelAttribute ProductDto productDto, Model model){
        boolean access = employeeService.checkAccess("EDIT_PRODUCT", productDto.getUserId());
        if (!access)
            return "not-found";
        productService.edited(productDto);
        Employee employee = employeeService.getById(productDto.getUserId());
        List<Product> list = productService.getListByProjectId(employee.getProject().getId());
        model.addAttribute("list", list);
        model.addAttribute("projectName", employee.getProject().getName());
        model.addAttribute("empId", productDto.getUserId());
        return "product";
    }
}
