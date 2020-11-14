package net.marcoreis.ecommerce.util;

import java.io.Serializable;

public interface IPersistente extends Serializable {
  Long getId();
  
  void setId(Long paramLong);
}
