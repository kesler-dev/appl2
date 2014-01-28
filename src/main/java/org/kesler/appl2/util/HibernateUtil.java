package org.kesler.appl2.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.HibernateException;
import java.util.Properties;

import org.kesler.appl2.util.OptionsUtil;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;


	private static void createSessionFactory() {

		String database = OptionsUtil.getOption("db.driver");
		String userName = OptionsUtil.getOption("db.user");
		String password = OptionsUtil.getOption("db.password");
		String server   = OptionsUtil.getOption("db.server");

		String driverClass = "";
		String connectionUrl = "";
		String dialect = "";



		if (database.equals("h2 local")) { 					///// для локальной базы данных H2
			driverClass = "org.h2.Driver";
			connectionUrl = "jdbc:h2:" + OptionsUtil.getCurrentDir() + "db/appl2";
			dialect = "org.hibernate.dialect.H2Dialect";
		} else if (database.equals("h2 net")) { 					///// для сетевой базы данных H2
			driverClass = "org.h2.Driver";
			connectionUrl = "jdbc:h2:tcp://" + server + "/db/appl2";
			dialect = "org.hibernate.dialect.H2Dialect";
		} else if (database.equals("mysql")) { 			///// для базы данных  MySQL
			driverClass = "com.mysql.jdbc.Driver";
			connectionUrl = "jdbc:mysql://" + server + ":3306/appl2";
			dialect = "org.hibernate.dialect.MySQLDialect";
		}

		Properties hibernateProperties = new Properties();
		// hibernateProperties.setProperty("hibernate.connection.driver_class","org.h2.Driver");
		// hibernateProperties.setProperty("hibernate.connection.url","jdbc:h2:mem:test;INIT=create schema if not exists test");
		// hibernateProperties.setProperty("hibernate.dialect","org.hibernate.dialect.H2Dialect");
		// hibernateProperties.setProperty("hibernate.connection.username","sa");
		// hibernateProperties.setProperty("hibernate.connection.password","");

		hibernateProperties.setProperty("hibernate.connection.driver_class",driverClass);
		hibernateProperties.setProperty("hibernate.connection.url",connectionUrl);
		hibernateProperties.setProperty("hibernate.dialect",dialect);
		hibernateProperties.setProperty("hibernate.connection.username",userName);
		hibernateProperties.setProperty("hibernate.connection.password",password);

		hibernateProperties.setProperty("hibernate.c3p0.minPoolSize","5");
		hibernateProperties.setProperty("hibernate.c3p0.maxPoolSize","20");
		hibernateProperties.setProperty("hibernate.c3p0.timeout","1800");
		hibernateProperties.setProperty("hibernate.c3p0.max_statement","50");
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto","update");
		hibernateProperties.setProperty("hibernate.show_sql","true");


		Configuration hibernateConfiguration = new AnnotationConfiguration()
						.addAnnotatedClass(org.kesler.appl2.logic.Service.class)
						.addAnnotatedClass(org.kesler.appl2.logic.FL.class)
						.addAnnotatedClass(org.kesler.appl2.logic.UL.class)
						.addAnnotatedClass(org.kesler.appl2.logic.Applicator.class)
						.addAnnotatedClass(org.kesler.appl2.logic.applicator.ApplicatorFL.class)
						.addAnnotatedClass(org.kesler.appl2.logic.applicator.ApplicatorUL.class)
						.addAnnotatedClass(org.kesler.appl2.logic.Operator.class)
						.addAnnotatedClass(org.kesler.appl2.logic.Reception.class)
						.addAnnotatedClass(org.kesler.appl2.logic.reception.ReceptionStatus.class)
                        .addAnnotatedClass(org.kesler.appl2.logic.reception.ReceptionStatusChange.class)
						.addAnnotatedClass(org.kesler.appl2.util.Counter.class)
						.setProperties(hibernateProperties);

		/// Пытаемся сконфигурировать Hibernate
		// System.out.println("Configuring Hibernate ...");
		// try {
			// Читаем конфигурацию из hibernate.cfg.xml
// 			hibernateConfiguration = hibernateConfiguration.configure();
// 		} catch (HibernateException he) {
// 			System.err.println("Hibernate configurationError");
// 			he.printStackTrace();
// 		}

		System.out.println("Building Hibernate session factory ...");
		try {
			//creates session factory
			sessionFactory = hibernateConfiguration.buildSessionFactory();
		} catch (HibernateException he) {
			System.err.println("Hibernate session factory create Error");
			he.printStackTrace();
		}
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			createSessionFactory();
		}
		return sessionFactory;
	}

	public static void closeConnection() {
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
		}	
	}
}