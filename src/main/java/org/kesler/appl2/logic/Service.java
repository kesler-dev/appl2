package org.kesler.appl2.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Proxy;

import org.hibernate.envers.Audited;

import org.kesler.appl2.dao.AbstractEntity;
import org.kesler.appl2.dao.EntityState;
import org.kesler.appl2.logic.reception.filter.StatusReceptionsFilter;


@Entity
@Proxy(lazy=false)
@Table(name="Services")
public class Service extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name="parentID")
	private Service parentService;

	@Column(name="Name", length=255)
	private String name;

    @Column(name="Code", length=50)
    private String code;

	@Column(name="Enabled")
	private Boolean enabled;

	public Service() {} // for Hibernate

	public Service (Service parentService, String name, Boolean enabled) {
		this.parentService = parentService;
		this.name = name;
		this.enabled = enabled;
	}

	public Service(String name) {
		this.name = name;
		this.enabled = true;
	}

	public Service getParentService() {
		return parentService;
	}

	public void setParentService(Service parentService) {
		this.parentService = parentService;
		state = EntityState.CHANGED;
	}

	public String getParentServiceName() {
		String name = "Родительская услуга не определена";
		if (parentService != null) {
			name = parentService.getName();
		}
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		state = EntityState.CHANGED;
	}

    public String getCode() { return code; }

    public void setCode(String code) {this.code = code;}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		state = EntityState.CHANGED;
	}

	@Override
	public String toString() {
		return name;
	}


}