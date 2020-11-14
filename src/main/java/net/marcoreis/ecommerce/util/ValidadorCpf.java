package net.marcoreis.ecommerce.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("ValidadorCpf")
public class ValidadorCpf implements Validator {
	public void validate(FacesContext context,
			UIComponent component, Object value)
			throws ValidatorException {
		if (value.toString().length() != 11) {
			String msg = "CPF inválido";
			FacesMessage fmsg = new FacesMessage(msg);
			fmsg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(fmsg);
		}
	}
}
