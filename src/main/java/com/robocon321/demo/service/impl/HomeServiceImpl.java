package com.robocon321.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robocon321.demo.repository.UserAccountRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
	UserRepository userRepository;

    @Autowired
    UserAccountRepository userAccountRepository;	
}
