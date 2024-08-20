package com.mailservices.demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WebHookServlet
 */
@WebServlet("/GenericWebHook")

public class GenericWebHook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenericWebHook() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logEverything(request, response);
	}

	private void logEverything(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getMethod();
		System.out.println("[" + method + "] Request Method: ");

		// Print all request parameters
		System.out.println("[" + method + "] Request Parameters:");
		request.getParameterMap().forEach((key, values) -> {
			System.out.println(key + ": " + String.join(", ", values));
		});

		// Print all cookies
		System.out.println("Cookies:");
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				System.out.println(cookie.getName() + ": " + cookie.getValue());
			}
		} else {
			System.out.println("No cookies found");
		}

		// Print general request information
		System.out.println("[" + method + "] General Request Information:");
		System.out.println("[" + method + "] Request URI: " + request.getRequestURI());
		System.out.println("[" + method + "] Request URL: " + request.getRequestURL());
		System.out.println("[" + method + "] Protocol: " + request.getProtocol());
		System.out.println("[" + method + "] Remote Address: " + request.getRemoteAddr());
		System.out.println("[" + method + "] Remote Host: " + request.getRemoteHost());
		System.out.println("[" + method + "] Remote Port: " + request.getRemotePort());
		System.out.println("[" + method + "] Local Address: " + request.getLocalAddr());
		System.out.println("[" + method + "] Local Port: " + request.getLocalPort());
		System.out.println("[" + method + "] Context Path: " + request.getContextPath());
		System.out.println("[" + method + "] Query String: " + request.getQueryString());
		System.out.println("[" + method + "] User Agent: " + request.getHeader("User-Agent"));

		/*
		System.out.println("[" + method + "] Request Body:");
		StringBuilder body = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			body.append(line).append(System.lineSeparator());
		}
		System.out.println(body.toString());
		*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);		
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doOptions(req, resp);

		logEverything(req, resp);

//		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
		resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
	}
}
