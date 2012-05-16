package br.com.ecc.server;

import java.sql.Connection;

import javax.persistence.EntityManager;

import org.hibernate.Session;

public class JPAUtil { 
	@SuppressWarnings("deprecation")
	public static Connection getConnection(EntityManager em){
		Session session = (Session) em.getDelegate();
		return session.connection();
	}
}