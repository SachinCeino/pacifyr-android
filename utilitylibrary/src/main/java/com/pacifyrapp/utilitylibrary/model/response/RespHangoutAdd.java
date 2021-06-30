package com.pacifyrapp.utilitylibrary.model.response;

/**

 */

public class RespHangoutAdd
{
    private String id;

    private String status;

    private Success[] success;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Success[] getSuccess ()
    {
        return success;
    }

    public void setSuccess (Success[] success)
    {
        this.success = success;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", status = "+status+", success = "+success+"]";
    }


    public class Success
    {
        private String _id;

        private String createdAt;

        public String get_id ()
        {
            return _id;
        }

        public void set_id (String _id)
        {
            this._id = _id;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [_id = "+_id+", createdAt = "+createdAt+"]";
        }
    }
}


