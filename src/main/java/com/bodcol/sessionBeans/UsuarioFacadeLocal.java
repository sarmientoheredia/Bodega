/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);
    
    Usuario iniciarSesion(Usuario us);
    
    Usuario findByCedula(String cedula);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();
    
    List<Usuario> findAllCargo();
    
    
}
