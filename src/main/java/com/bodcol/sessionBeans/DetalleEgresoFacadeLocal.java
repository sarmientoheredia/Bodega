/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.DetalleEgreso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Local
public interface DetalleEgresoFacadeLocal {

    void create(DetalleEgreso detalleEgreso);

    void edit(DetalleEgreso detalleEgreso);

    void remove(DetalleEgreso detalleEgreso);

    DetalleEgreso find(Object id);

    List<DetalleEgreso> findAll();

    List<DetalleEgreso> findRange(int[] range);

    int count();
    
}
