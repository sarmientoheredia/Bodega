/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.Egreso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Local
public interface EgresoFacadeLocal {

    void create(Egreso egreso);

    void edit(Egreso egreso);

    void remove(Egreso egreso);

    Egreso find(Object id);

    List<Egreso> findAll();

    List<Egreso> findRange(int[] range);

    int count();
    
    
    
    List<Egreso> findDate(Object date,Object date1);
    
}
