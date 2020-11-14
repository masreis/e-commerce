package net.marcoreis.ecommerce.controlador.conversor;

import javax.faces.convert.FacesConverter;
import net.marcoreis.ecommerce.entidades.Categoria;

@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter extends GenericConverter {
	public Class<?> getClasse() {
		return Categoria.class;
	}
}
