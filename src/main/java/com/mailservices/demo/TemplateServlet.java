package com.mailservices.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.sparkpost.Client;
import com.sparkpost.model.AddressAttributes;
import com.sparkpost.model.TemplateAttributes;
import com.sparkpost.model.TemplateContentAttributes;
import com.sparkpost.model.responses.Response;
import com.sparkpost.resources.ResourceTemplates;
import com.sparkpost.transport.IRestConnection;
import com.sparkpost.transport.RestConnection;

@WebServlet("/TemplateServlet")


public class TemplateServlet extends GenericWebHook {
	
	private static SessionFactory sessionFactory;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
		
		String name = "demo template " + System.currentTimeMillis();
		
		StringBuilder body = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			body.append(line.replace("\\n", "").replace("\\", ""));
			
		}
		System.out.println(body.toString());

		String html = body.toString();

		try {

			if(sessionFactory == null) {
				final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
						.configure() // configures settings from hibernate.cfg.xml
						.build();

				sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();				
			}
		}
		catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		Template prod = new Template();
		prod.setFechaCreacion(new Date());
		prod.setHtml(html);
		prod.setName(name);

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.persist(prod);
		session.getTransaction().commit();
		session.close();	

		response.setContentType("text/html;charset=UTF-8");
		response.setStatus(response.SC_OK);

        String API_KEY = "7515c7dfcee89de7e23563214fea88ad2477e7a5";
        Client client = new Client(API_KEY);

		TemplateAttributes tpl = new TemplateAttributes();
		tpl.setName(prod.getName());
		tpl.setDescription("this is a demo template from Leo");
		tpl.setContent(new TemplateContentAttributes());
		tpl.getContent().setFrom(new AddressAttributes("info@leonardolarraquy.com.ar", "me", null));
		tpl.getContent().setHtml(prod.getHtml());
		tpl.getContent().setSubject("Template Test");

		try {
			IRestConnection connection = new RestConnection(client, "https://api.sparkpost.com/api/v1");
			Response res = ResourceTemplates.create(connection, tpl);
			
			System.out.println(res.getResponseBody());
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
				
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doOptions(request, response);
	}

}
