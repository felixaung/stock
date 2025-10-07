package com.example.project_stock.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.project_stock.model.Role;
import com.example.project_stock.repository.RoleRepository;

import jakarta.annotation.PostConstruct;

@Component
public class RoleSeeder {

    @Autowired
    private RoleRepository roleRepository;
    
    @PostConstruct
    public void initRoles() {
    	createRoleIsNotExist("ADMIN");
    	createRoleIsNotExist("USER");
    }
    
    public void createRoleIsNotExist(String roleName) {
    	boolean isExist = roleRepository.findByName(roleName).isPresent();
    	
    	if (!isExist) {
    		Role role = new Role();
    		role.setName(roleName);
    		roleRepository.save(role);	
    	}
    }
}