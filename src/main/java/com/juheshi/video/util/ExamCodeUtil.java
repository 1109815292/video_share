package com.juheshi.video.util;

public class ExamCodeUtil {

    private static short STOREID = 175;

    private static short USERID = 95;

    private static short ORDERID = 245;

    private static short SECKILLID = 250;

    public static short getShoet(int num){
        Short s = (short)(num % 99);
        return s;
    }

    /**
     *
     * @param a storeID
     * @param b userID
     * @param c orderID
     * @param d seckillID
     * @return
     */
    public static String getExamCode(int a ,int b ,int c ,int d ){
        int i = getShoet ( a ) & STOREID;
        int j = getShoet ( b ) & USERID;
        int k = getShoet ( c ) & ORDERID;
        int m = getShoet ( d ) & SECKILLID;
        StringBuffer str = new StringBuffer (  );
        if(i<10){
            str.append ( "A" ).append ( i );
        }else {
            str.append ( i );
        }
        if(j<10){
            str.append ( "B" ).append ( j );
        }else {
            str.append ( j );
        }
        if(k<10){
            str.append ( "C" ).append ( k );
        }else {
            str.append ( k );
        }
        if(m<10){
            str.append ( "C" ).append ( m );
        }else {
            str.append ( m );
        }
        return str.toString ();
    }

    public static void main(String[] args) {
        String examCode = getExamCode ( 22 , 55 , 88 , 789 );
        System.out.println (examCode);
    }
}
