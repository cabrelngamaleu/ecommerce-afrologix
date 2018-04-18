package net.cabrel.ecommerceafrologix.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.cabrel.ecommerceafrologix.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	private final static Logger logger = LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	private CartService cartService;
	@RequestMapping("/show")
	public ModelAndView showCart(@RequestParam(name = "result", required = false) String result) {
		
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Shopping Cart");
		mv.addObject("userClickShowCart", true);
		
		if(result!=null) {
			switch(result) {
				case "added":
					mv.addObject("message", "Le produit a été ajouté avec succès dans le panier!");					
					cartService.validateCartLine();
					break;
				case "unavailable":
					mv.addObject("message", "La quantité de produit n'est pas disponible!");					
					break;
				case "updated":
					mv.addObject("message", "Le panier a été mis à jour avec succès!");					
					cartService.validateCartLine();
					break;
				case "modified":
					mv.addObject("message", "Un ou plusieurs objets dans le panier ont été modifiés!");
					break;
				case "maximum":
					mv.addObject("message", "La limite maximum pour l'article a été atteinte!");
					break;
				case "deleted":
					mv.addObject("message", "CartLine a ete supprimé avec succès");
					break;

			}
		}
		else {
			String response = cartService.validateCartLine();
			if(response.equals("result=modified")) {
				mv.addObject("message", "Un ou plusieurs objets dans le panier ont été modifiés!");
			}
		}

		mv.addObject("cartLines", cartService.getCartLines());
		return mv;
		
	}
	

	@RequestMapping("/{cartLineId}/update")
	public String udpateCartLine(@PathVariable int cartLineId, @RequestParam int count) {
		String response = cartService.manageCartLine(cartLineId, count);		
		return "redirect:/cart/show?"+response;		
	}
	
	@RequestMapping("/add/{productId}/product")
	public String addCartLine(@PathVariable int productId) {
		String response = cartService.addCartLine(productId);
		return "redirect:/cart/show?"+response;
	}
	
	@RequestMapping("/{cartLineId}/remove")
	public String removeCartLine(@PathVariable int cartLineId) {
		String response = cartService.removeCartLine(cartLineId);
		return "redirect:/cart/show?"+response;
	}
	
	@RequestMapping("/validate")
	public String validateCart() {	
		String response = cartService.validateCartLine();
		if(!response.equals("result=success")) {
			return "redirect:/cart/show?"+response;
		}
		else {
			return "redirect:/cart/checkout";
		}
	}	
}
