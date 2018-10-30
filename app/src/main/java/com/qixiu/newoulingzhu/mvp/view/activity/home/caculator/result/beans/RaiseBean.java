package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans;

public class RaiseBean{
        String age;
        String person;

    public RaiseBean() {
    }

    public RaiseBean(String age, String num) {
        this.age = age;
        this.person = num;
    }

    public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getPerson() {
            return person;
        }

        public void setPerson(String person) {
            this.person = person;
        }
    }