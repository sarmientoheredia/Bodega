package com.bodcol.webBean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Locale;
import javax.faces.event.ValueChangeEvent;

@Named(value = "idiomaBean")
@SessionScoped
public class IdiomaBean implements Serializable {

    private String language;
    private Locale locale;

    public IdiomaBean() {
        language="es";
        locale=new Locale(language);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    
    public void  cambiarIdioma(ValueChangeEvent event){
       language= (String) event.getNewValue();
       locale=new Locale(language);
    }
}
