package Manager;

import java.util.*;

public class hackerrank {


    public static long arrayManipulation(int n, List<List<Integer>> queries) {
        List<Long> arr = new ArrayList<Long>();
        int startIdx,endIdx,sumVal;
        for (int i = 0; i < n; i++) {
            arr.add((long) 0);
        }

        for(int i=0;i<queries.size();i++)
        {
            startIdx=queries.get(i).get(0);
            endIdx=queries.get(i).get(1);
            sumVal=queries.get(i).get(2);
            for(int j=startIdx-1;j<endIdx;j++) {
                arr.set(j,arr.get(j)+sumVal);
            }
        }
        Collections.sort(arr);
        return arr.get(n-1);
    }

    public static int sockMerchant(int n, List<Integer> ar) {
        HashMap<Integer,Integer> sockMap = new HashMap<>();

        for(int i=0;i<ar.size();i++) {
            if(sockMap.containsKey(ar.get(i)))
            {
                sockMap.put(ar.get(i), sockMap.get(ar.get(i))+1);
            }
            else
            {
                sockMap.put(ar.get(i),1);
            }
        }
        int pairs=0;
        for (Map.Entry<Integer, Integer> entry : sockMap.entrySet()) {
            Integer value = entry.getValue();
            pairs+=(value/2);
        }
        return pairs;
    }

    public static int countingValleys(int steps, String path) {
        int valleys=0;
        int level=0;
        boolean flag=false;
        for(int i=0;i<path.length();i++)
        {
            if(path.charAt(i)=='U') {
                level+=1;
            }

            else {
                level-=1;
            }

            if(level<0&&!flag) {
                valleys+=1;
                flag=true;
            }

            if(level==0) {
                flag=false;
            }
        }
        return valleys;

    }

    public static int jumpingOnClouds(List<Integer> c) {
        int nextCloud2,jump=0;
        for(int i=0;i<c.size();jump++) {
            nextCloud2=c.get(i+2);
            if(nextCloud2!=1) {
                i=i+2;
            }
            else{
                i=i+1;
            }
        }
        return jump;

    }

    public static long repeatedString(String s, long n) {
        long count=0;
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i)=='a')
                count++;
        }
        count=count*(n/s.length());
        long remainder=n%s.length();
        for(int i=0;i<remainder;i++) {
            if(s.charAt(i)=='a')
                count++;
        }


        return count;

    }

    public static void plusMinus(List<Integer> arr) {
        double pos=0;
        double neg=0;
        double zero=0;
        double total = arr.size();
        for(int i=0;i<arr.size();i++) {
            if(arr.get(i)<0)
            {
                neg++;
            }
            else if(arr.get(i)>0){
                pos++;
            }
            else
            {
                zero++;
            }
        }
        System.out.printf("%.6lf",pos/total);
        System.out.printf("%.6lf",neg/total);
        System.out.printf("%.6lf",zero/total);
    }

    public static void miniMaxSum(List<Integer> arr) {
        Collections.sort(arr);
        long minSum=0,maxSum=0;
        for(int i=0;i<4;i++) {
            minSum+=arr.get(i);
        }
        int j=0;
        for(int i=arr.size()-1;j<4;i--,j++) {
            maxSum+=arr.get(i);
        }

        System.out.println(minSum+" "+maxSum);

    }

    public static String timeConversion(String s) {
        // Write your code here
        StringBuilder result= new StringBuilder((String)s.subSequence(0,8));
        if(s.contains("A")){
            if(s.startsWith("12"))
            {
                result.setCharAt(0,'0');
                result.setCharAt(1,'0');
            }
        }
        else{
            if(!s.startsWith("12"))
            {
                int firstdg=result.charAt(0)-48;
                int secondg=result.charAt(1)-48;
                int val=firstdg*10+secondg;
                val+=12;
                StringBuilder str=new StringBuilder("Q");
                char a=(char)((val/10)+48);
                char b=(char)((val%10)+48);
                result.setCharAt(0,a);
                result.setCharAt(1,b);
            }
        }
        return result.toString();

    }
    public static void main(String args[]) {
        System.out.println(timeConversion("06:40:03AM"));
    }

    public static int findMedian(List<Integer> arr) {
        Collections.sort(arr);

        return arr.get(arr.size()/2);


    }

}
