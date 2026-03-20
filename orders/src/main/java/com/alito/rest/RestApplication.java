package com.alito.rest;

import org.eclipse.microprofile.auth.LoginConfig;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@LoginConfig(authMethod="MP-JWT")
@DeclareRoles({"ADMIN","USER"})
public class RestApplication extends Application {

}