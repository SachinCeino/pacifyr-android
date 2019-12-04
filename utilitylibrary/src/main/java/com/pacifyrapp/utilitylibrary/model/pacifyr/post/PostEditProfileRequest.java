package com.pacifyrapp.utilitylibrary.model.pacifyr.post;

/**
 * Created by Samsad on 11/August/2017.
 * samsad@ceinotech.com
 */

public class PostEditProfileRequest {

    private Certification[] certification;

    private String gender;

    private Education[] education;

    public Certification[] getCertification ()
    {
        return certification;
    }

    public void setCertification (Certification[] certification)
    {
        this.certification = certification;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public Education[] getEducation ()
    {
        return education;
    }

    public void setEducation (Education[] education)
    {
        this.education = education;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [certification = "+certification+", gender = "+gender+", education = "+education+"]";
    }

    public class Certification {
        private String title;

        private String duration;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "ClassPojo [title = " + title + ", duration = " + duration + "]";
        }

    }
    public class Education
    {
        private String title;

        private String duration;

        private String location;

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public String getLocation ()
        {
            return location;
        }

        public void setLocation (String location)
        {
            this.location = location;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [title = "+title+", duration = "+duration+", location = "+location+"]";
        }
    }
}
