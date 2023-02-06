package com.pacifyr.client.model;

/**
 * Created by Sachin Thampan on 21/10/19.
 */
public class OpenEmployerEvent {

    private ModelEvent.Employer employer;

    public OpenEmployerEvent(ModelEvent.Employer employer) {
        this.employer = employer;
    }

    public ModelEvent.Employer getEmployer() {
        return employer;
    }

    public void setEmployer(ModelEvent.Employer employer) {
        this.employer = employer;
    }
}
