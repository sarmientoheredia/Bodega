/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.Ingreso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Local
public interface IngresoFacadeLocal {

    void create(Ingreso ingreso);

    void edit(Ingreso ingreso);

    void remove(Ingreso ingreso);

    Ingreso find(Object id);

    List<Ingreso> findAll();

    List<Ingreso> findRange(int[] range);

    int count();
    
    Ingreso findByNumero(String numero);
    
}
