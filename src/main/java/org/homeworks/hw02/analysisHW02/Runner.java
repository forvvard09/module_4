package org.homeworks.hw02.analysisHW02;

public class Runner {

    public static void main(String[] args) {
        InstDao instDao = new InstDao();
        //instDao.dropTables();
        //instDao.createTables();

        instDao.createNewUser(SqlUtil.getRandomName(), SqlUtil.getRandomPsw());

    }
}
