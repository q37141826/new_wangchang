package com.qixiu.wigit.picker;

public class SelectedDataBean {
        private String id;
        private String text;
        private int state;
        private boolean is_visble;

    public SelectedDataBean(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public SelectedDataBean() {
    }

    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean is_visble() {
            return is_visble;
        }

        public void setIs_visble(boolean is_visble) {
            this.is_visble = is_visble;
        }
    }