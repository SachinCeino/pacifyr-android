package com.pacifyr.omhealthcare.model;

public class MComments
    {
        public String item;
        public boolean isSelected=false;

        public MComments(String item, boolean isSelected) {
        this.item = item;
        this.isSelected = isSelected;
    }
        public MComments(String item) {
            this.item = item;
            this.isSelected = false;
        }
    }