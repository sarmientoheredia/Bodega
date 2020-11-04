/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.DetalleIngreso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Local
public interface DetalleIngresoFacadeLocal {

    void create(DetalleIngreso detalleIngreso);

    void edit(DetalleIngreso detalleIngreso);

    void remove(DetalleIngreso detalleIngreso);

    DetalleIngreso find(Object id);

    List<DetalleIngreso> findAll();

    List<DetalleIngreso> findRange(int[] range);

    int count();
    
}
