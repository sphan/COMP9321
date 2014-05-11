package edu.unsw.comp9321.logic;

import javax.servlet.http.HttpServletRequest;

public class PassByRef {
	
	private String errorMessage = null;
	private String URLMessage = null;
	
	public void addErrorMessage(String errorMessage) {
		if (this.errorMessage == null) {
			this.errorMessage = errorMessage;
		} else {
			this.errorMessage += "\n"+errorMessage;
		}
	}
	
	public String getErrorMessage () {
		return this.errorMessage;
	}
	
	public void postErrorMessage (HttpServletRequest request) {
		request.setAttribute("errorMessage", this.errorMessage);
	}
	
	public void addURLMessage(String URLMessage) {
		if (this.URLMessage == null) {
			this.URLMessage = URLMessage;
		} else {
			this.URLMessage += "\n"+URLMessage;
		}
	}

	public String getURLMessage() {
		return URLMessage;
	}

	public void postURLMessage (HttpServletRequest request) {
		request.setAttribute("URLMessage", this.URLMessage);
	}
}
