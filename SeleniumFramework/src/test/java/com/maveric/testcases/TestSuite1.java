package com.maveric.testcases;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.maveric.pageobjects.HomePage;
import com.maveric.pageobjects.LoginPage;
import com.maveric.pageobjects.RegisterPage;
import com.maveric.pageobjects.UserPage;
import com.maveric.utils.WDHelper;

import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;


@Listeners({ATUReportsListener.class, ConfigurationListener.class,
	  MethodListener.class })
public class TestSuite1 {
	{
        System.setProperty("atu.reporter.config", "src/test/resources/atu.properties");
    }
	@BeforeMethod
	public void bm() {
		WDHelper.initialize("gc");
		WDHelper.navigate("www.magento.com");
	}
	
	@Test(priority=10)
	public void register() {
		WDHelper.click(HomePage.user_icon);
		WDHelper.click(LoginPage.reg_btn);
		WDHelper.assertTitle("Create New Customer Account");
		WDHelper.type(RegisterPage.fname, "Natarajan");
		WDHelper.type(RegisterPage.lname, "Ramanathan");
		WDHelper.type(RegisterPage.email, "natarajan.ramanathan93@gmail.com");
		WDHelper.selectByText(RegisterPage.country, "India");
		WDHelper.selectByValue(RegisterPage.comp_primary, "selling");
		WDHelper.selectByIndex(RegisterPage.individual_role, 2);
		WDHelper.type(RegisterPage.pwd, "Welcome");
		WDHelper.type(RegisterPage.cnf_pwd, "Welcome");
		WDHelper.click(RegisterPage.agree_terms);
		WDHelper.click(RegisterPage.robot);
		WDHelper.click(RegisterPage.reg_btn);
	}
	
	@Test(priority=20)
	public void negative_login() {
		WDHelper.click(HomePage.user_icon);
		WDHelper.type(LoginPage.email_ip, "test@gmail.com");
		WDHelper.type(LoginPage.pwd_ip, "welcome");
		WDHelper.click(LoginPage.singin_btn);
		WDHelper.assertText(LoginPage.error_msg, "Invalid login or password.");
	}
	
	@Test(priority=30)
	public void positive_login() {
		WDHelper.click(HomePage.user_icon);
		WDHelper.type(LoginPage.email_ip, "natarajan.ramanathan93@gmail.com");
		WDHelper.type(LoginPage.pwd_ip, "Welcome123");
		WDHelper.click(LoginPage.singin_btn);
		WDHelper.assertText(UserPage.id, "ID: MAG003417822");
	}
	
	@AfterMethod
	public void am(){
		WDHelper.quit();
	}
	
}
