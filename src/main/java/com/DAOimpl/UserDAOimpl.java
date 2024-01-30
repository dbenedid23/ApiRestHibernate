/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.DAOimpl;

import com.interfaces.UserDAO;
import com.modelPojo.User;
import com.utils.HibernateUtil;
import java.util.*;
import org.hibernate.*;
import org.hibernate.query.Query;

/**
 *
 * @author Luis Quintano
 */
public class UserDAOimpl implements UserDAO, AutoCloseable {
    //constructor
    public UserDAOimpl() throws Exception {  //no hace falta el try/catch porque ya se lanza la Exception.
    }
    
    
    //metodos
    @Override
    public boolean insertUser(User user) throws Exception {
        System.out.println("Insertando usuario: " + user);
        boolean usuarioInsertado;
        Transaction tx = null;  //inicializamos la transacción a null. La transacción solo se hace si se ejecuta ttodo el código, si falla algo no hace nada.

        try (Session session = HibernateUtil.getSessionFactory().openSession()){  //para hacer la conexión con la database.
            tx = session.beginTransaction();
            System.out.println("intento insertarlo");
            session.persist(user);  //esto es como hacer un insert para insertar el producto a la tabla.
            tx.commit();  //para completar la transacción.
            usuarioInsertado = true;
        } catch (Exception ex) {
            if (tx != null) {  //si la transacción es distinta de null que significa que está abierta y que no se ha completado....
                tx.rollback();  //esto va a deshacer lo que ha hecho antes y va a volver a como estaba.
            }
            System.err.println("ERROR al añadir el usuario \"" + user.getName()+ "\".");
            usuarioInsertado = false;
        }
        return usuarioInsertado;
    }
    
    @Override
    public List<User> getAllUsers() throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){  //para hacer la conexión con la database.
            Query<User> query = session.createQuery("FROM User", User.class);  //ponemos "FROM y el nombre de la clase" y después se pone la clase a la que nos referimos.
            return query.getResultList();  //devuelve una lista genérica con todos los datos de la tabla. Si se utilzia ".list()" es lo mismo que ".getResultList()".
        } catch (Exception ex) {
            System.err.println(ex);
            return null;  //si salta una exception devuelve null que es como no devolver nada.
            //también pudes retornar un ArrayList vacío pero luego tienes que controlarlo cuando lo muestres.
        }
    }
    
    @Override
    public User getUserById(String id) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){  //para hacer la conexión con la database.
            Query<User> query = session.createQuery("FROM User WHERE id = :valorNombre", User.class);  //después del FROM se pone el nombre de la clase. El ":value" va a ser único (no se puede repetir) y se va a cambiar por el valor que yo quiera poner.
            query.setParameter("valorNombre", id);  //cambiamos el ":valor" por el nombre que nos pasan. Hacemos un setParameter por cada valor que queramos cambiar.
            return query.getSingleResult();  //retorna la query con el usuario.
        } catch (Exception ex) {
            System.err.println("No se puede mostrar el usuario con id \"" + id + "\".");
            return null;  //si salta una exception devuelve null que es como no devolver nada.
            //también puedes retornar un ArrayList vacío pero luego tienes que controlarlo cuando lo muestres.
        }
    }

    @Override
    public List<User> getUsersByYearBirth(int yearbirth) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){  //para hacer la conexión con la database.
            Query<User> query = session.createQuery("FROM User WHERE yearbirth = :valorNombre", User.class);  //después del FROM se pone el nombre de la clase. El ":value" va a ser único (no se puede repetir) y se va a cambiar por el valor que yo quiera poner.
            query.setParameter("valorNombre", yearbirth);  //cambiamos el ":valor" por el nombre que nos pasan. Hacemos un setParameter por cada valor que queramos cambiar.
            return query.getResultList();  //retorna la query de usuarios en forma de lista.
        } catch (Exception ex) {
            System.err.println("No se pueden mostrar los usuarios con el año de nacimiento \"" + yearbirth + "\".");
            return null;  //si salta una exception devuelve null que es como no devolver nada.
            //también puedes retornar un ArrayList vacío pero luego tienes que controlarlo cuando lo muestres.
        }
    }

    @Override
    public void close() throws Exception {
        //System.err.println("UserDAOimpl Cerrado");
    }
}
