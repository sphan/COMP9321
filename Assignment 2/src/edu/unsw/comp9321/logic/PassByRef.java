package edu.unsw.comp9321.logic;

import javax.servlet.http.HttpServletRequest;

public class PassByRef {
	
	private String errorMessage = null;
	
	public void addErrorMessage(String errorMessage) {
		if (this.errorMessage == null) {
			this.errorMessage = errorMessage;
		} else {
			this.errorMessage += "\n"+errorMessage;
		}
	}
	
	public void addErrorMessage(int ErrorMessage) {
		this.addErrorMessage(String.valueOf(errorMessage));
	}
	
	public String getErrorMessage () {
		return this.errorMessage;
	}
	
	public void postErrorMessage (HttpServletRequest request) {
		request.setAttribute("errorMessage", this.errorMessage);
	}

}
