package com.example.tppatrones;

import com.example.tppatrones.entities.Ordenador;
import com.example.tppatrones.entities.Procesador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;



public class TpPatronesApplication {

    private static EntityManager em;

    public static void main(String[] args) throws IllegalAccessException {
        String hql="";
        hql+="SELECT * from session";
       // Query q = em.createQuery(hql);

        //List<String> lst = q.getResultList();

        //System.out.println(lst);
        Object or = new Ordenador("mi nuevo ordenador", 100);
      /*  Procesador pro = new Procesador();
        pro.setMarca("Intel core i7");
        pro.setNucleos(8);

        ((Ordenador) or).setProcessor(pro);
       */
        ArrayList<String> memorias = new ArrayList<>();
        memorias.add("una ram");
        memorias.add("otra ram");
        ((Ordenador) or).setListaDeMemorias(memorias);
        PersistentObject persistor = new PersistentObject();
        persistor.store(1, or);



    }

}
