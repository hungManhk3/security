package com.example.security.service;

import com.example.security.dto.request.AuthenticationRequest;
import com.example.security.dto.request.IntrospectRequest;
import com.example.security.dto.response.AuthenticationResponse;
import com.example.security.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;


}
