package bsz.exch.core;

import javax.jws.WebService;

@WebService
public abstract interface SoapService{
  public abstract String perform(String paramString);
  
}
 