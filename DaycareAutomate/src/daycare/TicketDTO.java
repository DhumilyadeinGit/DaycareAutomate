package daycare;

import java.util.Date;

public class TicketDTO {

	private int rowNum;

	private long id;

	private Date lastModified;

	private String assignedTo;

	private String clientInfo;

	private String title;

	private String priority;

	private String severity;

	private String scope;

	private String status;

	private boolean isDateTodayOnwards;

	private boolean isTicketEligible;

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(String clientInfo) {
		this.clientInfo = clientInfo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isDateTodayOnwards() {
		return isDateTodayOnwards;
	}

	public void setDateTodayOnwards(boolean isDateTodayOnwards) {
		this.isDateTodayOnwards = isDateTodayOnwards;
	}

	public boolean isTicketEligible() {
		return isTicketEligible;
	}

	public void setTicketEligible(boolean isTicketEligible) {
		this.isTicketEligible = isTicketEligible;
	}

}
