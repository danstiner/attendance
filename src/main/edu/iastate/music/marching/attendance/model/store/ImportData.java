package edu.iastate.music.marching.attendance.model.store;

import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Blob;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Version;


@Version(AttendanceDatastore.VERSION)
@Entity(kind = "ImportData", allocateIdsBy = 0)
public class ImportData {

	ImportData() {
	}
	
	private Date timestamp;

	@Type(Text.class)
	private String importData;
	
	private String errorMessage;

	public void setTimestamp(Date uploadTime) {
		this.timestamp = uploadTime;
	}
	
	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setData(String uploadData) {
		this.importData = uploadData;
	}

	public String getData() {
		return this.importData;
	}

	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
