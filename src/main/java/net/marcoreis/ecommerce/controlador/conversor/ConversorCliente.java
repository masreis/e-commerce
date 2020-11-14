package net.marcoreis.ecommerce.controlador.conversor;

import javax.faces.convert.FacesConverter;

import net.marcoreis.ecommerce.entidades.Cliente;

@FacesConverter(forClass = Cliente.class)
public class ConversorCliente extends GenericConverter {
	public Class<?> getClasse() {
		return Cliente.class;
	}
}
