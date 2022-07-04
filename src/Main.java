import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws RomanSubzeroException, OutOfRangeException, WrongInputException{
        while(true) {
            System.out.println("введите выражение");
            Scanner input = new Scanner(System.in);
            String expression = input.nextLine();
            String expression_gapless = expression.replaceAll("\\s", "");
            Pattern arab1 = Pattern.compile("(10|[1-9])[\\+,\\-,\\*,\\/](10|[1-9])");
            Matcher arab2 = arab1.matcher(expression_gapless);
            Pattern arab1out = Pattern.compile("(0|[1-9]{2,100})[\\+,\\-,\\*,\\/](0|[1-9]{1,100})|(0|[1-9]{1,100})[\\+,\\-,\\*,\\/](0|[1-9]{2,100})");
            Matcher arab2out = arab1out.matcher(expression_gapless);
            Pattern rome1 = Pattern.compile("(^((I(V|X))|(V?I{1,3})|[XV])[\\+\\-\\*\\/]((IV)|(IX)|(V?I{1,3})|[XV]))");
            Matcher rome2 = rome1.matcher(expression_gapless);
            Pattern rome1out = Pattern.compile("[IVXCLM]{1,100}[\\+\\-\\*\\/][IVXCLM]{1,100}");
            Matcher rome2out = rome1out.matcher(expression_gapless);
            Pattern differ1 = Pattern.compile("(0|[1-9]{1,100})[\\+,\\-,\\*,\\/][IVXCLM]{1,100}|[IVXCLM]{1,100}[\\+,\\-,\\*,\\/](0|[1-9]{1,100})");
            Matcher differ2 = differ1.matcher(expression_gapless);
            if (differ2.matches()) {
                throw new WrongInputException("используются разные системы счисления");
            }
            if (arab2.matches()) {
                String[] arr_arab_digit = expression_gapless.split("([\\+\\-\\*\\/])");
                int arab_digit_int1 = Integer.parseInt(arr_arab_digit[0]);
                int arab_digit_int2 = Integer.parseInt(arr_arab_digit[1]);
                Pattern path1 = Pattern.compile("([\\+\\-\\*\\/])");
                Matcher operator1 = path1.matcher(expression_gapless);
                operator1.find();
                String r1 = operator1.group();
                switch (r1) {
                    case "+":
                        System.out.println(arab_digit_int1 + arab_digit_int2);
                        break;
                    case "-":
                        System.out.println(arab_digit_int1 - arab_digit_int2);
                        break;
                    case "*":
                        System.out.println(arab_digit_int1 * arab_digit_int2);
                        break;
                    case "/":
                        System.out.println(arab_digit_int1 / arab_digit_int2);
                        break;
                }
            }
            if (arab2out.matches()) {
                throw new OutOfRangeException("ввод арабских цифр за пределами диапазона калькулятора");
            }
            if (rome2.matches()) {
                String[] arr_rome_digit = expression_gapless.split("([\\+\\-\\*\\/])");
                String[] arr_rome_template = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
                int[] arab_from_rome = {0, 0};
                for (int j = 0; j < arab_from_rome.length; j++) {
                    for (int i = 0; i < arr_rome_template.length; i++) {
                        if (arr_rome_digit[j].equals(arr_rome_template[i])) {
                            arab_from_rome[j] = i + 1;
                            break;
                        } else continue;
                    }
                }
                Pattern path2 = Pattern.compile("([\\+\\-\\*\\/])");
                Matcher operator2 = path2.matcher(expression_gapless);
                operator2.find();
                String r2 = operator2.group();
                int arab_from_rome_res = 0;
                switch (r2) {
                    case "+":
                        arab_from_rome_res = arab_from_rome[0] + arab_from_rome[1];
                        break;
                    case "-":
                        arab_from_rome_res = arab_from_rome[0] - arab_from_rome[1];
                        break;
                    case "*":
                        arab_from_rome_res = arab_from_rome[0] * arab_from_rome[1];
                        break;
                    case "/":
                        arab_from_rome_res = arab_from_rome[0] / arab_from_rome[1];
                        break;
                }
                if (arab_from_rome_res >= 0) {
                    String[] arr_rome = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
                    int[] arr_arab = {100, 90, 50, 40, 10, 9, 5, 4, 1};
                    String rome_digit = "";
                    do {
                        for (int i = 0; i < arr_arab.length; i++) {
                            if (arab_from_rome_res >= arr_arab[i]) {
                                arab_from_rome_res = arab_from_rome_res - arr_arab[i];
                                rome_digit = rome_digit + arr_rome[i];
                                break;
                            }
                        }
                    } while (arab_from_rome_res > 0);
                    System.out.println(rome_digit);
                } else {
                    throw new RomanSubzeroException("результат выражения ниже либо равен нулю");
                }
            }
            if (rome2.matches() == false && rome2out.matches() == true) {
                throw new OutOfRangeException("ввод римских цифр некорректен либо за пределами диапазона калькулятора");
            }
            if (rome2.matches() == false && rome2out.matches() == false && arab2.matches() == false && arab2out.matches() == false) {
                throw new WrongInputException("введенное выражение не соответсвует требованиям условий ввода");
            }
        }
    }
}