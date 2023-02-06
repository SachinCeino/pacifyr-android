package com.pacifyrapp.utilitylibrary.model.pacifyr;

/**

 */

public class ModelFacebook
{
    private Picture picture;

    private String id;

    private String first_name;

    private Cover cover;

    private String name;

    private String last_name;

    private String gender;

    private String email;

    public Picture getPicture ()
    {
        return picture;
    }

    public void setPicture (Picture picture)
    {
        this.picture = picture;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public Cover getCover ()
    {
        return cover;
    }

    public void setCover (Cover cover)
    {
        this.cover = cover;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [picture = "+picture+", id = "+id+", first_name = "+first_name+", cover = "+cover+", name = "+name+", last_name = "+last_name+", gender = "+gender+"]";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public class Picture
    {
        private Data data;

        public Data getData ()
        {
            return data;
        }

        public void setData (Data data)
        {
            this.data = data;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [data = "+data+"]";
        }
    }

    public class Data
    {
        private String is_silhouette;

        private String height;

        private String width;

        private String url;

        public String getIs_silhouette ()
        {
            return is_silhouette;
        }

        public void setIs_silhouette (String is_silhouette)
        {
            this.is_silhouette = is_silhouette;
        }

        public String getHeight ()
        {
            return height;
        }

        public void setHeight (String height)
        {
            this.height = height;
        }

        public String getWidth ()
        {
            return width;
        }

        public void setWidth (String width)
        {
            this.width = width;
        }

        public String getUrl ()
        {
            return url;
        }

        public void setUrl (String url)
        {
            this.url = url;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [is_silhouette = "+is_silhouette+", height = "+height+", width = "+width+", url = "+url+"]";
        }
    }


    public class Cover
    {
        private String id;

        private String source;

        private String offset_y;

        private String offset_x;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getSource ()
        {
            return source;
        }

        public void setSource (String source)
        {
            this.source = source;
        }

        public String getOffset_y ()
        {
            return offset_y;
        }

        public void setOffset_y (String offset_y)
        {
            this.offset_y = offset_y;
        }

        public String getOffset_x ()
        {
            return offset_x;
        }

        public void setOffset_x (String offset_x)
        {
            this.offset_x = offset_x;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+", source = "+source+", offset_y = "+offset_y+", offset_x = "+offset_x+"]";
        }
    }
}