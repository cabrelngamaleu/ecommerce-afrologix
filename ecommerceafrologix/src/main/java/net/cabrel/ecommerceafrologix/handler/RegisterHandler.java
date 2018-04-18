package net.cabrel.ecommerceafrologix.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.cabrel.ecommerceafrologix.model.RegisterModel;
import net.cabrel.shoppingbackend.dao.UserDAO;
import net.cabrel.shoppingbackend.dto.Address;
import net.cabrel.shoppingbackend.dto.Cart;
import net.cabrel.shoppingbackend.dto.User;

@Component
public class RegisterHandler {


 @Autowired
 private PasswordEncoder passwordEncoder;
	
	
 @Autowired
 private UserDAO userDAO;
 public RegisterModel init() { 
  return new RegisterModel();
 } 
 public void addUser(RegisterModel registerModel, User user) {
  registerModel.setUser(user);
 } 
 public void addBilling(RegisterModel registerModel, Address billing) {
  registerModel.setBilling(billing);
 }

 public String validateUser(User user, MessageContext error) {
  String transitionValue = "success";
   if(!user.getPassword().equals(user.getConfirmPassword())) {
    error.addMessage(new MessageBuilder().error().source(
      "confirmPassword").defaultText("Le mot de passe ne correspond pas à la confirmation du mot de passe!").build());
    transitionValue = "failure";    
   }  
   if(userDAO.getByEmail(user.getEmail())!=null) {
    error.addMessage(new MessageBuilder().error().source(
      "email").defaultText("L'adresse e-mail est déjà prise!").build());
    transitionValue = "failure";
   }
  return transitionValue;
 }
 
 public String saveAll(RegisterModel registerModel) {
  String transitionValue = "success";
  User user = registerModel.getUser();
  if(user.getRole().equals("USER")) {

   Cart cart = new Cart();
   cart.setUser(user);
   user.setCart(cart);
  }
   

  user.setPassword(passwordEncoder.encode(user.getPassword()));
  

  userDAO.add(user);

  Address billing = registerModel.getBilling();
  billing.setUserId(user.getId());
  billing.setBilling(true);  
  userDAO.addAddress(billing);
  return transitionValue ;
 } 
}
