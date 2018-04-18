package net.cabrel.ecommerceafrologix.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import net.cabrel.shoppingbackend.dto.Product;

public class ProductValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Product product = (Product) target;
		if(product.getFile() == null || product.getFile().getOriginalFilename().equals("")) {
			errors.rejectValue("file", null, "Veuillez sélectionner un fichier à télécharger!");
			return;
		}
		if(! (product.getFile().getContentType().equals("image/jpeg") || 
				product.getFile().getContentType().equals("image/png")) ||
				product.getFile().getContentType().equals("image/gif")
			 )
			{
				errors.rejectValue("file", null, "S'il vous plaît sélectionner un fichier image à télécharger!");
				return;	
			}

	}

}
