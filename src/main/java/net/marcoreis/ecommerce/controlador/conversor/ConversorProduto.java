package net.marcoreis.ecommerce.controlador.conversor;

import javax.faces.convert.FacesConverter;

import net.marcoreis.ecommerce.entidades.Produto;

@FacesConverter(forClass = Produto.class)
public class ConversorProduto extends GenericConverter {
	public Class<?> getClasse() {
		return Produto.class;
	}
}
