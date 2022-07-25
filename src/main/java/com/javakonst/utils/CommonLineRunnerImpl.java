package com.javakonst.utils;

import com.javakonst.service.NumbersByPropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CommonLineRunnerImpl implements CommandLineRunner {

    @Autowired
    NumbersByPropService service;

    @Override
    public void run(String... args) throws Exception {
        NumberToWords numberToWords = new NumberToWords(service);

        Scanner scanner = new Scanner(System.in);
        long num = 0;
        String sGender = "";
        String sCase = "";
        while(true){
            System.out.print("Введите число (меньше 1 триллион): ");
            if (scanner.hasNextLong()) {
                num = scanner.nextLong();
            }
            System.out.print("\nВведите род (М, Ж, С): ");
            if (scanner.hasNextLine()) {
                sGender = scanner.next().trim().toUpperCase();
            }
            System.out.print("\nВведите падеж (И, Р, Д, В, Т, П): ");
            if (scanner.hasNextLine()) {
                sCase = scanner.next().trim().toUpperCase();
            }
            System.out.println("\n\nЧисло прописью: " + numberToWords.sumProp(num, sGender, sCase));
            System.out.println("\n----------------");
        }
    }
}

