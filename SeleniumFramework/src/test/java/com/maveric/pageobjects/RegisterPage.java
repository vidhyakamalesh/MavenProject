package com.maveric.pageobjects;

import org.openqa.selenium.By;

public class RegisterPage {
public static By fname = By.id("firstname");
public static By lname = By.id("lastname");
public static By email = By.name("email");
public static By country = By.name("country");
public static By comp_primary = By.name("company_type");
public static By individual_role = By.name("individual_role");
public static By pwd = By.name("password");
public static By cnf_pwd = By.name("confirmation");
public static By agree_terms = By.id("agree_terms");
public static By frame = By.xpath("//iframe[@role='presentation']");
public static By robot = By.className("recaptcha-checkbox-checkmark");
public static By reg_btn = By.id("registerSubmit");
}
