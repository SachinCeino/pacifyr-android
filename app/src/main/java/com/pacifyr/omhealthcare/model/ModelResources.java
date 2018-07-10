package com.pacifyr.omhealthcare.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ModelResources
{
    public void setResults (ArrayList<Results> list)
    {
        if(list!=null)
            this.results=list.toArray( (new Results[list.size()]));
    }

    public ArrayList<Results> getResultsList() {
        ArrayList<Results> arrayList = null;
        try {
            if(results != null) {
                arrayList = new ArrayList<Results>(Arrays.asList(results));

            }
        } catch (Exception e){//Crashlytics.logException(e);
            arrayList = null;
        }

        return arrayList;
    }
    public void addResults (Results[] results)
    {
        ArrayList<Results> arrayList = new ArrayList<Results>();
        if(getResultsList()!=null)
            arrayList.addAll(getResultsList());
        if(results!=null)
            arrayList.addAll(Arrays.asList(results));
        if(arrayList!=null)
            this.results=arrayList.toArray( (new Results[arrayList.size()]));


    }
    private Results[] results;

    public Results[] getResults ()
    {
        return results;
    }

    public void setResults (Results[] results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "[results = "+results+"]";
    }



    public class Results
    {
        private String updatedAt;

        private String id;

        private String author;

        private String title;

        private String createdAt;

        private String image;

        private String url;

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getAuthor ()
        {
            return author;
        }

        public void setAuthor (String author)
        {
            this.author = author;
        }

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getCreatedAt ()
        {
            String dates = null;
            try {

                //int offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();

                long myTimeOffset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());
                long day = 86400000;
                long hour = 3600000;
                long minute = 60000;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'", Locale.ENGLISH);
                Date date = null;
                long days = 0;
                long hours = 0;
                long minutes = 0;
                String value = null;

                Calendar postedDay = Calendar.getInstance();
                date = df.parse(createdAt);
                postedDay.setTime(date);

                SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a",Locale.ENGLISH);
                long now = System.currentTimeMillis() + myTimeOffset;
                long newtime = postedDay.getTimeInMillis() + myTimeOffset;
                date = new Date(newtime);
                //dates = df.format(date);
                dates = outputFormat.format(date);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }


            return dates;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
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
            return "[updatedAt = "+updatedAt+", id = "+id+", author = "+author+", title = "+title+", createdAt = "+createdAt+", image = "+image+", url = "+url+"]";
        }
    }
}