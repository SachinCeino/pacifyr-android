package com.ceino.utilitylibrary.model.pacifyr;

/**
 * Created by Mani-Ceino on 12/14/2017.
 */

public class MRoleModel {

        private String updatedAt;

        private String id;

        private String createdAt;

        private String label;

        private String mainLabel;

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

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getLabel ()
        {
            return label;
        }

        public void setLabel (String label)
        {
            this.label = label;
        }

        public String getMainLabel ()
        {
            return mainLabel;
        }

        public void setMainLabel (String mainLabel)
        {
            this.mainLabel = mainLabel;
        }

        @Override
        public String toString()
        {
            return "";

           // return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", createdAt = "+createdAt+", label = "+label+", mainLabel = "+mainLabel+"]";
        }

}
