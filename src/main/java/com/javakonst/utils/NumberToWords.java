package com.javakonst.utils;

import com.javakonst.entity.NumberByProp;
import com.javakonst.service.NumbersByPropService;

import java.util.HashMap;
import java.util.Map;

public class NumberToWords {
    private static final String HUNDRED = "10_2";
    private static final String THOUSAND = "10_3";
    private static final String MILLION = "10_6";
    private static final String BILLION = "10_9";

    private static final String MAN = "М";
    private static final String WOMAN = "Ж";
    private static final String MIDDLE = "С";

    private String gender;
    private String sCase;
    private long number;

    NumbersByPropService service;

    public NumberToWords(NumbersByPropService service) {
        this.service = service;
    }

    private NumberToWords(){}


    public String sumProp(long number, String gender, String sCase){
        this.gender = gender;
        this.sCase = sCase;
        this.number = number;

        Map<String, Integer> octGroupFromLong = longToOctGroups();

        int octGroupBillion = octGroupFromLong.get("4");
        int octGroupMillion = octGroupFromLong.get("3");
        int octGroupThousand = octGroupFromLong.get("2");
        int octGroupHandred = octGroupFromLong.get("1");

        StringBuilder words = new StringBuilder();

        if (octGroupBillion != 0) {
            words.append(octGroupToWords(octGroupBillion, BILLION));
        }
        if (octGroupMillion != 0) {
            words.append(octGroupToWords(octGroupMillion, MILLION));
        }
        if (octGroupThousand != 0) {
            words.append(octGroupToWords(octGroupThousand, THOUSAND));
        }

        words.append(octGroupToWords(octGroupHandred, HUNDRED));

        return words.toString();
    }

    private Map<String, Integer> longToOctGroups(){
        // 123 456 789 159 -> (1)=159, (2)=789, (3)=456, (4)=123 => HasMap

        //k = 10 for data = 999
        //k = 1000 for data = 999 999 999 999

        int max = 4;
        int k = 1000;
        long data = number;
        int i = 1;
        boolean isStop = false;
        int groupdigits = 0;
        long t = 0;
        Map<String, Integer> numberGroups = new HashMap<>();
        while (!isStop) {
            groupdigits = 0;
            if (data > 0) {
                t = data % (long) Math.pow(k, i);
                t = t / (long) Math.pow(k, i - 1);
                data = data - t * (long) Math.pow(k, i - 1);
                groupdigits = Integer.parseInt(Long.toString(t));
            }
            numberGroups.put(String.valueOf(i), groupdigits);
            i++;
            if (i > max) isStop = true;
        }

        return numberGroups;
    }

    private String octGroupToWords(int octGroup, String groupName){
        Map<String, Integer> digits = intToDigits(octGroup);
        int digit100 = digits.get("3")*100;
        int digit10 = digits.get("2")*10;
        int digit1 = digits.get("1");

        String word100 = "";
        if (digit100 != 0) {
            if (sCase.equals("Д")) word100 = "(у "+digit100+" нет дательного падежа) ";
            else word100 = digitToWord(digit100, MIDDLE, sCase);
        }

        String word10 = "";
        if (digit10 != 0){
            if (digit10 == 10){
                word10 = digitToWord(digit10+digit1, MIDDLE, sCase);
            } else {
                if (digit10 != 20 && digit10 != 30 && sCase.equals("Д")) word10 = "(у "+digit10+" нет дательного падежа) ";
                else word10 = digitToWord(digit10, MIDDLE, sCase);
            }
        }

        String word1 = "";
        if (digit1 != 0 && digit10 != 10){
            if (groupName.equals(THOUSAND) && (digit1 == 1 || digit1 == 2)) word1 = digitToWord(digit1, WOMAN, sCase);
            else if (groupName.equals(HUNDRED) && (digit1 == 1 || digit1 == 2)) word1 = digitToWord(digit1, gender, sCase);
            else if (digit1 == 1) word1 = digitToWord(digit1, gender, sCase);
            else if (2<=digit1 && digit1<=4) word1 = digitToWord(digit1, MAN, sCase);
            else word1 = digitToWord(digit1, MIDDLE, sCase);
        }

        StringBuilder words = new StringBuilder();
        words.append(word100).append(word10).append(word1);

        if (words.length()==0 || groupName.equals(HUNDRED)) return words.toString();

        StringBuilder wordGroupName = new StringBuilder();
        int digit1_10 = digit1 + digit10;
        String s = groupNameToWord(groupName).trim();
        String[] split = s.split("/");
        if (digit1_10 != 11 && digit1 == 1) {
            wordGroupName.append(split[split.length - 1]);

        } else if (!(12<=digit1_10 && digit1_10<=14) && (2 <= digit1 && digit1 <= 4)){
            wordGroupName.append(split[0]);

        } else if ((5<=digit1 && digit1<=9) || (10<=digit1_10 && digit1_10<=14)) {
            wordGroupName.append(split[split.length - 2]);

        } else wordGroupName.append("(ошибка выбора для " + groupName + ")");

        return words.append(wordGroupName).append(" ").toString();
    }

    private Map<String, Integer> intToDigits(int num){
        // num -> (1)=9, (2)=8, (3)=7 => HashMap

        int max = 3;
        int k = 10;
        double data = num;
        int i = 1;
        boolean isStop = false;
        int digit = 0;
        double t = 0;
        Map<String, Integer> digits = new HashMap<>();

        while (!isStop) {
            digit = 0;
            if (data > 0) {
                t = data % Math.pow(k, i);
                t = t / Math.pow(k, i - 1);
                data = data - t * Math.pow(k, i - 1);
                digit = (int) t;
            }
            digits.put(String.valueOf(i), digit);
            i++;
            if (i > max) isStop = true;
        }

        return digits;
    }

    private String digitToWord(int digit, String gender, String sCase){
        return getNumberByCase(String.valueOf(digit), gender, sCase);
    }

    private String groupNameToWord(String groupName){
        String changedGender = MAN;
        if (groupName.equals(THOUSAND)) changedGender = WOMAN;
        return getNumberByCase(groupName, changedGender, sCase);
    }

    private String getNumberByCase(String num, String gender, String sCase) {
        NumberByProp nums = service.getByNumberAndGender(num, gender);
        switch (sCase) {
            case "И":
                return nums.getCase_i()+" ";
            case "Р":
                return nums.getCase_r()+" ";
            case "Д":
                return nums.getCase_d()+" ";
            case "В":
                return nums.getCase_v()+" ";
            case "Т":
                return nums.getCase_t()+" ";
            case "П":
                return nums.getCase_p()+" ";
            default:
                return "Падеж не определен ";
        }
    }
}
