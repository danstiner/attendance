package serverLogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import People.Person;
import People.Student;

/**
 * This is a class used to add stuff to the database
 * 
 * @author Brandon
 * 
 */
public class DatabaseUtil {
	public static void addPerson(Person p) {
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(p);
		em.close();
	}

	/**
	 * A method that gets a person based off the given netID
	 * 
	 * @param netID
	 *            the netID registered with a student
	 * @return either the person with that netID or null if not found
	 */
	public static Person getPerson(String netID) {

		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select p from Student p where p.netID = :netID");
		q.setParameter("netID", netID);

		Person p;
		// See if that person actually exists
		try {
			p = (Student) q.getSingleResult();
		} catch (NoResultException e) {
			p = null;
		}
		return p;
	}

	public static String[] listAll() {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from Student m");
		List<Person> people = q.getResultList();
		String[] toRet = new String[people.size()];
		for (int i = 0; i < people.size(); i++) {
			Person p = people.get(i);
			toRet[i] = p.toString();
		}
		em.close();
		return toRet;
	}

}
