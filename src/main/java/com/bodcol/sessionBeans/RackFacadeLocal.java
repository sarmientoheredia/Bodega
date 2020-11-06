/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.Rack;
import com.bodcol.entidades.Seccion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Local
public interface RackFacadeLocal {

    void create(Rack rack);

    void edit(Rack rack);

    void remove(Rack rack);

    Rack find(Object id);
    
    Rack findByNombre(String nombre);

    List<Rack> findAll();

    List<Rack> findRange(int[] range);

    int count();
    
    
    List<Rack> findAllSeccion(Seccion seccion);
}
