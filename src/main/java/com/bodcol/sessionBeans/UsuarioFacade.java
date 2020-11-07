package com.bodcol.sessionBeans;

import com.bodcol.entidades.Usuario;
import com.bodcol.utilitarios.Encriptar;
import com.bodcol.utilitarios.Mensaje;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "bodegaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    public Usuario findByCedula(String cedula) {
        Query q = em.createNamedQuery("Usuario.findByCedula", Usuario.class);
        q.setParameter("cedula", cedula);
        try {
            return (Usuario) q.getSingleResult();

        } catch (NoResultException e) {
            Mensaje.mostrarExito("Cedula valida");
            return null;
        }
    }

    //METODO PARA AUTENTICAR AL USUARIO QUE PODRA INGRESAR AL SISTEMA EN BASE AL USER Y PASSWORD 
    @Override
    public Usuario iniciarSesion(Usuario us) {
        Query q = em.createNamedQuery("Usuario.findByLogin", Usuario.class);
        q.setParameter("usuario", us.getUsuario());
        q.setParameter("password", Encriptar.sha512(us.getPassword()));
        try {
            return (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
            Mensaje.mostrarAdvertencia("Credenciales incorrectas");
            return null;
        }
    }

    @Override
    public List<Usuario> findAllCargo() {
        Query q = em.createNamedQuery("Usuario.findAllCargo", Usuario.class);
        return q.getResultList();
    }

}
