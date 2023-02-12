package net.marcoreis.ecommerce.controlador.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.marcoreis.ecommerce.negocio.GenericService;
import net.marcoreis.ecommerce.util.IPersistente;

public abstract class GenericConverter implements Converter {
	private static Logger logger =
			LogManager.getLogger(GenericConverter.class);

	public abstract Class<?> getClasse();

	public Object getAsObject(FacesContext context,
			UIComponent component, String value) {
		try {
			Long id = Long.valueOf(Long.parseLong(value));
			Object objeto = (new GenericService())
					.findById(getClasse(), id);
			return objeto;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	public String getAsString(FacesContext context,
			UIComponent component, Object value) {
		IPersistente p = (IPersistente) value;
		return String.valueOf(p.getId());
	}
}
