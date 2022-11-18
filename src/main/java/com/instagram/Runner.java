package com.instagram;

public class Runner {
    public static void main(String[] args) {
        try (InstDao instDao = new InstDao()) {
            instDao.dropTables();
            instDao.createTables();
            instDao.fillData();
            instDao.addNewUser("Petr3", "1234");
            //instDao.addLike(null, 1, 1);
            System.out.println(instDao.getUserInfo(4));
        }
    }
}
